package com.roadmateserver.root.service.impl;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.BookingDTO;
import com.roadmateserver.root.dto.VehicleDTO;
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
import com.roadmateserver.root.service.BookingService;
import com.roadmateserver.root.service.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final VehicleService vehicleService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository,
                              VehicleService vehicleService) {
        this.vehicleService = vehicleService;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
        final VehicleEntity vehicleEntity = VehicleDTOEntityMapper.map(bookingDTO.getVehicle());
        log.debug("Vehicle mapped: {}", vehicleEntity);
        final BookingEntity bookingEntity = BookingDTOEntityMapper.map(bookingDTO);
        log.debug("Booking entity created: {}", bookingEntity);
        bookingEntity.setRenter(userEntity);
        bookingEntity.setVehicle(vehicleEntity);
        log.debug("Successfully set renter and vehicle in booking entity");
        final BookingEntity savedBookingEntity = bookingRepository.save(bookingEntity);
        log.info("Booking created successfully with ID: {}", savedBookingEntity.getBookingId());
        return BookingDTOEntityMapper.map(savedBookingEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDTO> getAllBookings() {
        log.info("Getting all bookings");
        final List<BookingDTO> bookingDTOS = bookingRepository.findAll()
                .stream()
                .map(booking -> {
                    final VehicleDTO vehicleDTO = vehicleService.getVehicleById(booking.getVehicle().getVehicleId());
                    log.debug("Vehicle mapped: {}", vehicleDTO);
                    final BookingDTO bookingDTO = BookingDTOEntityMapper.map(booking);
                    log.debug("Booking mapped: {}", bookingDTO);
                    bookingDTO.setVehicle(vehicleDTO);
                    return bookingDTO;
                }).toList();
        log.info("Retrieved {} bookings", bookingDTOS.size());
        return bookingDTOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BookingDTO deleteBooking(final BookingDTO bookingDTO) {
        if(Objects.isNull(bookingDTO)){
            log.error("Booking details are null");
            throw new IllegalArgumentException("BookingDTO cannot be null");
        }
        if(!bookingDTO.getStatus().equals(Constants.BookingStatus.PENDING)){
            log.error("Booking with ID {} cannot be deleted as it is not in PENDING status", bookingDTO.getId());
            throw new BookingException("Booking can only be deleted if it is in PENDING status");
        }
        log.info("Deleting booking with ID: {}", bookingDTO.getId());
        final BookingEntity bookingEntity = bookingRepository.findById(bookingDTO.getId())
                .orElseThrow(() -> {
                    log.error("Booking with ID {} not found", bookingDTO.getId());
                    return new BookingNotFoundException("Booking not found");
                });
        log.debug("Booking found: {}", bookingEntity);
        bookingRepository.delete(bookingEntity);
        log.info("Booking with ID {} deleted successfully", bookingDTO.getId());
        return BookingDTOEntityMapper.map(bookingEntity);

    }

    @Override
    @Transactional(readOnly = true)
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
    public Page<BookingDTO> getBookingsByRenterId(final String renterId, final List<Constants.BookingStatus> statuses, final Integer page, final Integer size) {
        if (Objects.isNull(page) || page < 0 || Objects.isNull(size) || size <= 0) {
            log.error("Invalid pagination parameters: page={}, size={}", page, size);
            throw new IllegalArgumentException("Page and size must be valid integers");
        }
        if (Objects.isNull(renterId) || renterId.isBlank()) {
            log.error("Renter ID is null or blank");
            throw new IllegalArgumentException("Renter ID must not be null or blank");
        }
        log.info("Fetching bookings for renter ID: {}", renterId);
        final UserEntity userEntity = userRepository.findByClerkId(renterId)
                .orElseThrow(() -> {
                    log.error("User with ID {} not found", renterId);
                    return new UserNotFoundException("User not found");
                });
        log.debug("User found: {}", userEntity);
        final Pageable pageable = PageRequest.of(page, size);
        final Page<BookingDTO> bookingPage;
        if(statuses == null || statuses.isEmpty()) {
            log.info("Fetching all bookings for renter ID: {} without status filter", renterId);
            bookingPage = bookingRepository.findAllByRenter_ClerkId(renterId, pageable)
                    .map(booking -> {
                        final VehicleDTO vehicleDTO = vehicleService.getVehicleById(booking.getVehicle().getVehicleId());
                        log.debug("Vehicle mapped: {}", vehicleDTO);
                        final BookingDTO bookingDTO = BookingDTOEntityMapper.map(booking);
                        bookingDTO.setVehicle(vehicleDTO);
                        log.debug("Booking mapped: {}", bookingDTO);
                        return bookingDTO;
                    });
        } else {
            log.info("Fetching bookings for renter ID: {} with status filter: {}", renterId, statuses);
            bookingPage = bookingRepository.findAllByRenter_ClerkIdAndStatusIn(renterId, statuses, pageable)
                    .map(booking -> {
                        final VehicleDTO vehicleDTO = vehicleService.getVehicleById(booking.getVehicle().getVehicleId());
                        log.debug("Vehicle mapped: {}", vehicleDTO);
                        final BookingDTO bookingDTO = BookingDTOEntityMapper.map(booking);
                        bookingDTO.setVehicle(vehicleDTO);
                        log.debug("Booking mapped: {}", bookingDTO);
                        return bookingDTO;
                    });
        }
        log.info("Fetched {} bookings for renter ID: {}", bookingPage.getTotalElements(), renterId);
        return bookingPage;
    }

}
