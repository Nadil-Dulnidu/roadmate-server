package com.roadmateserver.root.service;

import com.roadmateserver.root.dto.PaymentDTO;
import org.springframework.stereotype.Service;
import com.roadmateserver.root.exception.BookingNotFoundException;
import com.roadmateserver.root.exception.UserNotFoundException;

import java.util.List;

@Service
public interface PaymentService {

    /**
     * Creates a new payment record in the system.
     *
     * @param paymentDTO the payment data transfer object containing payment details
     * @return the created payment data transfer object
     * @throws IllegalArgumentException if the paymentDTO is null or contains invalid data
     * @throws BookingNotFoundException if the booking associated with the payment does not exist
     * @throws UserNotFoundException if the user associated with the payment does not exist
     */
    PaymentDTO createPayment(PaymentDTO paymentDTO);

    /**
     * Retrieves all payment records from the system.
     *
     * @return a list of payment data transfer objects
     */
    List<PaymentDTO> getAllPayments();

    /**
     * Retrieves payment records associated with a specific user ID.
     *
     * @param userId the unique identifier of the user
     * @return a list of payment data transfer objects associated with the user
     * @throws IllegalArgumentException if the userId is null or empty
     */
    List<PaymentDTO> getPaymentsByUserId(String userId);


}
