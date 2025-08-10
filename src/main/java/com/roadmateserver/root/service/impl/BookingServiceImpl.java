package com.roadmateserver.root.service.impl;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.BookingDTO;
import com.roadmateserver.root.dto.UserDTO;
import com.roadmateserver.root.dto.VehicleDTO;
import com.roadmateserver.root.entity.BookingEntity;
import com.roadmateserver.root.entity.UserEntity;
import com.roadmateserver.root.entity.VehicleEntity;
import com.roadmateserver.root.mapper.BookingDTOEntityMapper;
import com.roadmateserver.root.mapper.UserDTOEntityMapper;
import com.roadmateserver.root.mapper.VehicleDTOEntityMapper;
import com.roadmateserver.root.repository.BookingRepository;
import com.roadmateserver.root.repository.UserRepository;
import com.roadmateserver.root.repository.VehicleRepository;
import com.roadmateserver.root.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository) {
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
            return new IllegalArgumentException("User not found");
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
                    final VehicleDTO vehicleDTO = VehicleDTOEntityMapper.map(booking.getVehicle());
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
            throw new IllegalArgumentException("Booking can only be deleted if it is in PENDING status");
        }
        log.info("Deleting booking with ID: {}", bookingDTO.getId());
        final BookingEntity bookingEntity = bookingRepository.findById(bookingDTO.getId())
                .orElseThrow(() -> {
                    log.error("Booking with ID {} not found", bookingDTO.getId());
                    return new IllegalArgumentException("Booking not found");
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
                    return new IllegalArgumentException("Booking not found");
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
            throw new IllegalArgumentException("Booking ID and status must not be null");
        }
        log.info("Updating booking status for booking ID: {}", bookingId);
        final BookingEntity bookingEntity = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.error("Booking with ID {} not found", bookingId);
                    return new IllegalArgumentException("Booking not found");
                });
        log.debug("Booking found: {}", bookingEntity);
        bookingEntity.setStatus(bookingStatus);
        final BookingEntity updatedBookingEntity = bookingRepository.save(bookingEntity);
        log.info("Booking status updated successfully for booking ID: {}", bookingId);
        return BookingDTOEntityMapper.map(updatedBookingEntity);
    }
}
