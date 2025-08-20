package com.roadmateserver.root.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Data Transfer Object representing a payment in the system")
public class PaymentDTO {
    @JsonProperty("payment_id")
    @Schema(description = "Unique identifier for the payment", example = "1")
    private Integer paymentId;

    @JsonProperty("stripe_id")
    @Schema(description = "Unique identifier for the payment in Stripe", example = "pi_123456789")
    @NotBlank(message = "Stripe ID must not be null")
    private String stripeId;

    @JsonProperty("booking_id")
    @Schema(description = "Unique identifier for the booking associated with the payment", example = "101")
    @NotNull(message = "Booking ID must not be null")
    private Integer bookingId;

    @JsonProperty("user_id")
    @Schema(description = "Unique identifier for the user making the payment", example = "1001")
    @NotBlank(message = "User ID must not be null")
    private String userId;

    @JsonProperty("amount")
    @Schema(description = "Amount of the payment in cents", example = "1500")
    @NotNull(message = "Amount must not be null")
    private Double amount;

    @JsonProperty("status")
    @Schema(description = "Status of the payment", example = "COMPLETED")
    @NotBlank(message = "Payment status must not be null")
    private String status;

    @JsonProperty("date")
    @Schema(description = "Date when the payment was made", example = "2023-10-01")
    @NotBlank(message = "Payment date must not be null")
    private String date;
}
