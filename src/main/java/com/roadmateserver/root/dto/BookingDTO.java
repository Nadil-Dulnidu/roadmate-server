package com.roadmateserver.root.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.roadmateserver.root.common.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Data Transfer Object representing a booking in the system")
public class BookingDTO {
    @JsonProperty("booking_id")
    @Schema(description = "Unique identifier for the booking", example = "1")
    private Integer id;

    @JsonProperty("renter_id")
    @NotBlank(message = "Renter ID must not be blank")
    @Schema(description = "Unique identifier for the renter", example = "Renter123")
    private String renterId;

    @JsonProperty("vehicle")
    @NotNull(message = "Vehicle must not be null")
    @Schema(description = "Details of the vehicle being booked")
    private VehicleDTO vehicle;

    @JsonProperty("start_date")
    @NotBlank(message = "Start date must not be blank")
    @Schema(description = "Start date of the booking", example = "2023-10-01")
    private String startDate;

    @JsonProperty("end_date")
    @NotBlank(message = "End date must not be blank")
    @Schema(description = "End date of the booking", example = "2023-10-10")
    private String endDate;

    @JsonProperty("total_price")
    @NotNull(message = "Total price must not be blank")
    @Schema(description = "Total price for the booking", example = "150.00")
    private Double totalPrice;

    @JsonProperty("created_at")
    @NotNull(message = "Created at must not be blank")
    @Schema(description = "Timestamp when the booking was created", example = "2023-10-01T12:00:00Z")
    private LocalDateTime createdAt;

    @JsonProperty("status")
    @NotNull(message = "Status must not be blank")
    @Schema(description = "Current status of the booking", example = "PENDING")
    private Constants.BookingStatus status;
}
