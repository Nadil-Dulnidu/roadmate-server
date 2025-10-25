package com.roadmateserver.root.service.impl;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.BookingDTO;
import com.roadmateserver.root.dto.VehicleDTO;
import com.roadmateserver.root.dto.cache.BookingListCache;
import com.roadmateserver.root.entity.BookingEntity;
import com.roadmateserver.root.entity.UserEntity;
import com.roadmateserver.root.entity.VehicleEntity;
import com.roadmateserver.root.exception.BookingException;
import com.roadmateserver.root.exception.BookingNotFoundException;
import com.roadmateserver.root.exception.UserNotFoundException;
import com.roadmateserver.root.mapper.BookingDTOEntityMapper;
import com.roadmateserver.root.mapper.VehicleDTOEntityMapper;
import com.roadmateserver.root.repository.BookingRepository;
import com.roadmateserver.root.repository.UserRepository;
import com.roadmateserver.root.repository.VehicleRepository;
import com.roadmateserver.root.service.BookingService;
import com.roadmateserver.root.service.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final VehicleService vehicleService;
    private final VehicleRepository vehicleRepository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public BookingServiceImpl(BookingRepository bookingRepository,
                              UserRepository userRepository,
                              VehicleService vehicleService,
                              VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleService = vehicleService;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {"bookingListCache", "bookingCache"}, allEntries = true)
    public BookingDTO createBooking(final BookingDTO bookingDTO) {
        if(Objects.isNull(bookingDTO)){
            log.error("Booking details are null");
            throw new IllegalArgumentException("BookingDTO cannot be null");
        }
        log.info("Creating booking with details: {}", bookingDTO);
        final UserEntity userEntity = userRepository.findByClerkId(bookingDTO.getRenterId())
                .orElseThrow(() -> {
            log.error("User with ID {} not found", bookingDTO.getRenterId());
            return new UserNotFoundException("User not found");
        });
        log.debug("User found: {}", userEntity);
        final VehicleEntity vehicleEntity = vehicleRepository.findById(bookingDTO.getVehicle().getVehicleId())
                .orElseThrow(() -> {
            log.error("Vehicle with ID {} not found", bookingDTO.getVehicle().getVehicleId());
            return new BookingException("Vehicle not found");
        });
        log.debug("Vehicle mapped: {}", vehicleEntity);
        if(!vehicleEntity.getIsAvailable().equals(Constants.VehicleStatus.AVAILABLE)){
            log.error("Vehicle with ID {} is not available for booking", vehicleEntity.getVehicleId());
            throw new BookingException("Vehicle is not available for booking");
        }
        final BookingEntity bookingEntity = BookingDTOEntityMapper.map(bookingDTO);
        log.debug("Booking entity created: {}", bookingEntity);
        bookingEntity.setRenter(userEntity);
        vehicleEntity.setIsAvailable(Constants.VehicleStatus.RESERVED);
        vehicleRepository.save(vehicleEntity);
        bookingEntity.setVehicle(vehicleEntity);
        log.debug("Successfully set renter and vehicle in booking entity");
        final BookingEntity savedBookingEntity = bookingRepository.save(bookingEntity);
        log.info("Booking created successfully with ID: {}", savedBookingEntity.getBookingId());
        return BookingDTOEntityMapper.map(savedBookingEntity);
    }

    @Override
    @Cacheable(value = "bookingListCache", key = "'all_bookings_statuses_' + #statuses")
    public BookingListCache getAllBookings(List<Constants.BookingStatus> statuses) {
        log.info("Getting all bookings");
        final List<BookingDTO> bookingDTOS = bookingRepository.findAll()
                .stream()
                .filter(booking -> statuses == null || statuses.isEmpty() || statuses.contains(booking.getStatus()))
                .map(booking -> {
                    final VehicleDTO vehicleDTO = vehicleService.getVehicleById(booking.getVehicle().getVehicleId());
                    log.debug("Vehicle mapped: {}", vehicleDTO);
                    final BookingDTO bookingDTO = BookingDTOEntityMapper.map(booking);
                    log.debug("Booking mapped: {}", bookingDTO);
                    bookingDTO.setVehicle(vehicleDTO);
                    return bookingDTO;
                }).toList();
        log.info("Retrieved {} bookings", bookingDTOS.size());
        return new BookingListCache(bookingDTOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {"bookingListCache", "bookingCache"},key = "'booking_' + #bookingId", allEntries = true)
    public BookingDTO deleteBooking(final Integer bookingId) {
        if(Objects.isNull(bookingId)){
            log.error("Booking ID is null");
            throw new IllegalArgumentException("Booking ID cannot be null");
        }
        log.info("Deleting booking with ID: {}", bookingId);
        final BookingEntity bookingEntity = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.error("Booking with ID {} not found", bookingId);
                    return new BookingNotFoundException("Booking not found");
                });
        log.debug("Booking found: {}", bookingEntity);
        if(!bookingEntity.getStatus().equals(Constants.BookingStatus.PENDING)){
            log.error("Booking with ID {} cannot be deleted as it is not in PENDING status", bookingId);
            throw new BookingException("Booking can only be deleted if it is in PENDING status");
        }
        log.debug("Booking found: {}", bookingEntity);
        bookingRepository.delete(bookingEntity);
        log.info("Booking with ID {} deleted successfully", bookingId);
        return BookingDTOEntityMapper.map(bookingEntity);

    }

    @Override
    @Cacheable(value = "bookingCache", key = "'booking_' + #bookingId")
    public BookingDTO getBookingById(final Integer bookingId) {
        if(Objects.isNull(bookingId)){
            log.error("Booking ID is null");
            throw new IllegalArgumentException("Booking ID cannot be null");
        }
        log.info("Fetching booking with ID: {}", bookingId);
        final BookingEntity bookingEntity = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.error("Booking with ID {} not found", bookingId);
                    return new BookingNotFoundException("Booking not found");
                });
        final VehicleDTO vehicleDTO = VehicleDTOEntityMapper.map(bookingEntity.getVehicle());
        log.debug("Vehicle mapped: {}", vehicleDTO);
        final BookingDTO bookingDTO = BookingDTOEntityMapper.map(bookingEntity);
        log.debug("Booking mapped: {}", bookingDTO);
        bookingDTO.setVehicle(vehicleDTO);
        log.info("Booking with ID {} fetched successfully", bookingId);
        return bookingDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "bookingListCache", allEntries = true)
    @CachePut(value = "bookingCache", key = "'booking_' + #bookingId")
    public BookingDTO updateBookingStatus(final Integer bookingId, final Constants.BookingStatus bookingStatus) {
        if(Objects.isNull(bookingId) || Objects.isNull(bookingStatus)){
            log.error("Booking ID or status is null");
            throw new BookingException("Booking ID and status must not be null");
        }
        log.info("Updating booking status for booking ID: {}", bookingId);
        final BookingEntity bookingEntity = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.error("Booking with ID {} not found", bookingId);
                    return new BookingNotFoundException("Booking not found");
                });
        log.debug("Booking found: {}", bookingEntity);
        bookingEntity.setStatus(bookingStatus);
        final BookingEntity updatedBookingEntity = bookingRepository.save(bookingEntity);
        log.info("Booking status updated successfully for booking ID: {}", bookingId);
        return BookingDTOEntityMapper.map(updatedBookingEntity);
    }

    @Override
    @Cacheable(value = "bookingListCache", key = "'renter_' + #renterId + '_statuses_' + #statuses")
    public BookingListCache getBookingsByRenterId(final String renterId, final List<Constants.BookingStatus> statuses) {
        if (Objects.isNull(renterId) || renterId.isBlank()) {
            log.error("Renter ID is null or blank");
            throw new IllegalArgumentException("Renter ID must not be null or blank");
        }
        log.info("Fetching bookings for renter ID: {} with statuses: {}", renterId, statuses);
        final List<BookingDTO> bookingDTOS = bookingRepository.findAllByRenter_ClerkId(renterId)
                .stream()
                .map(booking -> {
                    final VehicleDTO vehicleDTO = vehicleService.getVehicleById(booking.getVehicle().getVehicleId());
                    log.debug("Vehicle mapped: {}", vehicleDTO);
                    final BookingDTO bookingDTO = BookingDTOEntityMapper.map(booking);
                    log.debug("Booking mapped: {}", bookingDTO);
                    bookingDTO.setVehicle(vehicleDTO);
                    return bookingDTO;
                })
                .filter(bookingDTO -> statuses == null || statuses.isEmpty() || statuses.contains(bookingDTO.getStatus()))
                .toList();
        log.info("Retrieved {} bookings", bookingDTOS.size());
        return new BookingListCache(bookingDTOS);
    }

    @Override
    @Cacheable(value = "bookingListCache", key = "'owner_' + #ownerId + '_statuses_' + #statuses")
    public BookingListCache getBookingsByOwnerId(final String ownerId, final List<Constants.BookingStatus> statuses) {
        if (Objects.isNull(ownerId) || ownerId.isBlank()) {
            log.error("Owner ID is null or blank");
            throw new IllegalArgumentException("Owner ID must not be null or blank");
        }
        log.info("Fetching bookings for owner ID: {} with statuses: {}", ownerId, statuses);
        final List<BookingDTO> bookingDTOS = bookingRepository.findAllByVehicle_Owner_ClerkId(ownerId)
                .stream()
                .map(booking -> {
                    final VehicleDTO vehicleDTO = vehicleService.getVehicleById(booking.getVehicle().getVehicleId());
                    log.debug("Vehicle mapped: {}", vehicleDTO);
                    final BookingDTO bookingDTO = BookingDTOEntityMapper.map(booking);
                    log.debug("Booking mapped: {}", bookingDTO);
                    bookingDTO.setVehicle(vehicleDTO);
                    return bookingDTO;
                })
                .filter(bookingDTO -> statuses == null || statuses.isEmpty() || statuses.contains(bookingDTO.getStatus()))
                .toList();
        log.info("Retrieved {} bookings", bookingDTOS.size());
        return new BookingListCache(bookingDTOS);
    }

    @Transactional
    @Scheduled(cron = "0 5 0 * * *")
    public void activateBookingsBasedOnPickupDate() {
        LocalDate today = LocalDate.now();
        log.info("Running booking activation scheduler for date: {}", today);

        List<BookingEntity> bookings = bookingRepository.findAll();
        for (BookingEntity booking : bookings) {
            LocalDate startDate = LocalDate.parse(booking.getStartDate(), FORMATTER);
            if (startDate.equals(today) && booking.getStatus() == Constants.BookingStatus.CONFIRMED) {
                booking.setStatus(Constants.BookingStatus.ACTIVE);
                bookingRepository.save(booking);
                log.info("Activated booking ID {} (Vehicle ID: {})", booking.getBookingId(), booking.getVehicle().getVehicleId());
            }
        }
    }

    @Transactional
    @Scheduled(cron = "0 55 23 * * *")
    public void completeBookingsBasedOnReturnDate() {
        LocalDate today = LocalDate.now();
        log.info("Running booking completion scheduler for date: {}", today);

        List<BookingEntity> bookings = bookingRepository.findAll();
        for (BookingEntity booking : bookings) {
            LocalDate endDate = LocalDate.parse(booking.getEndDate(), FORMATTER);
            if (endDate.equals(today) && booking.getStatus() == Constants.BookingStatus.ACTIVE) {
                booking.setStatus(Constants.BookingStatus.COMPLETED);
                bookingRepository.save(booking);
                log.info("Completed booking ID {} (Vehicle ID: {})", booking.getBookingId(), booking.getVehicle().getVehicleId());
            }
        }
    }
}
