package com.roadmateserver.root.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentDTO {
    @JsonProperty("payment_id")
    private Integer paymentId;

    @JsonProperty("stripe_id")
    @NotBlank(message = "Stripe ID must not be null")
    private String stripeId;

    @JsonProperty("booking_id")
    @NotNull(message = "Booking ID must not be null")
    private Integer bookingId;

    @JsonProperty("user_id")
    @NotBlank(message = "User ID must not be null")
    private String userId;

    @JsonProperty("amount")
    @NotNull(message = "Amount must not be null")
    private Double amount;

    @JsonProperty("status")
    @NotBlank(message = "Payment status must not be null")
    private String status;

    @JsonProperty("date")
    @NotBlank(message = "Payment date must not be null")
    private String date;
}
