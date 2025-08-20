package com.roadmateserver.root.mapper;

import com.roadmateserver.root.dto.PaymentDTO;
import com.roadmateserver.root.entity.PaymentEntity;

import java.util.Objects;

public class PaymentDTOEntityMapper {

    public static PaymentDTO map(PaymentEntity paymentEntity) {
        if (Objects.isNull(paymentEntity))
            throw new IllegalArgumentException("Payment entity cannot be null");
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setPaymentId(paymentEntity.getPaymentId());
        paymentDTO.setStripeId(paymentEntity.getStripeId());
        if(Objects.isNull(paymentEntity.getUser()))
            throw new IllegalArgumentException("User cannot be null");
        paymentDTO.setUserId(paymentEntity.getUser().getClerkId());
        if(Objects.isNull(paymentEntity.getBooking()))
            throw new IllegalArgumentException("Booking cannot be null");
        paymentDTO.setBookingId(paymentEntity.getBooking().getBookingId());
        paymentDTO.setAmount(paymentEntity.getAmount());
        paymentDTO.setStatus(paymentEntity.getStatus());
        paymentDTO.setDate(paymentEntity.getDate());
        return paymentDTO;
    }

    public static PaymentEntity map(PaymentDTO paymentDTO) {
        if (Objects.isNull(paymentDTO))
            throw new IllegalArgumentException("Payment DTO cannot be null");
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setPaymentId(paymentDTO.getPaymentId());
        paymentEntity.setStripeId(paymentDTO.getStripeId());
        paymentEntity.setAmount(paymentDTO.getAmount());
        paymentEntity.setStatus(paymentDTO.getStatus());
        paymentEntity.setDate(paymentDTO.getDate());
        return paymentEntity;
    }
}
