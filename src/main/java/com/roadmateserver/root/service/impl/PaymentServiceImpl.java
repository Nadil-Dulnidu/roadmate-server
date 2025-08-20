package com.roadmateserver.root.service.impl;

import com.roadmateserver.root.dto.PaymentDTO;
import com.roadmateserver.root.entity.BookingEntity;
import com.roadmateserver.root.entity.PaymentEntity;
import com.roadmateserver.root.entity.UserEntity;
import com.roadmateserver.root.exception.BookingNotFoundException;
import com.roadmateserver.root.exception.UserNotFoundException;
import com.roadmateserver.root.mapper.PaymentDTOEntityMapper;
import com.roadmateserver.root.repository.BookingRepository;
import com.roadmateserver.root.repository.PaymentRepository;
import com.roadmateserver.root.repository.UserRepository;
import com.roadmateserver.root.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingService;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, UserRepository userRepository, BookingRepository bookingService) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.bookingService = bookingService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentDTO createPayment(final PaymentDTO paymentDTO) {
        if(Objects.isNull(paymentDTO)){
            log.error("PaymentDTO is null");
            throw new IllegalArgumentException("PaymentDTO must not be null");
        }
        log.info("Payment is creating for booking ID: {}", paymentDTO.getBookingId());
        final BookingEntity bookingEntity = bookingService.findById(paymentDTO.getBookingId())
                .orElseThrow(() -> {
                    log.error("Booking with ID {} not found", paymentDTO.getBookingId());
                    return new BookingNotFoundException("Booking not found for ID: " + paymentDTO.getBookingId());
                });
        final UserEntity userEntity = userRepository.findByClerkId(paymentDTO.getUserId())
                .orElseThrow(() -> {
                    log.error("User with ID {} not found", paymentDTO.getUserId());
                    return new UserNotFoundException("User not found for ID: " + paymentDTO.getUserId());
                });
        log.info("Payment is being saved for booking ID: {}", paymentDTO.getBookingId());
        final PaymentEntity paymentEntity = PaymentDTOEntityMapper.map(paymentDTO);
        paymentEntity.setUser(userEntity);
        paymentEntity.setBooking(bookingEntity);
        final PaymentEntity savedPayment = paymentRepository.save(paymentEntity);
        log.info("Payment successfully created with ID: {}", savedPayment.getPaymentId());
        return PaymentDTOEntityMapper.map(savedPayment);
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        log.info("Getting all payments for booking IDs");
        List<PaymentDTO> paymentDTOS = paymentRepository.findAll()
                .stream()
                .map(PaymentDTOEntityMapper::map)
                .toList();
        log.info("Total payments found: {}", paymentDTOS.size());
        return paymentDTOS;
    }

    @Override
    public List<PaymentDTO> getPaymentsByUserId(final String userId) {
        if (Objects.isNull(userId) || userId.isBlank()) {
            log.error("User ID must not be null or blank");
            throw new IllegalArgumentException("User ID must not be null or blank");
        }
        log.info("Getting payments for user ID: {}", userId);
        List<PaymentDTO> paymentDTOS = paymentRepository.findAllByUser_ClerkId(userId)
                .stream()
                .map(PaymentDTOEntityMapper::map)
                .toList();
        log.info("Total payments found for user ID {}: {}", userId, paymentDTOS.size());
        return paymentDTOS;
    }
}
