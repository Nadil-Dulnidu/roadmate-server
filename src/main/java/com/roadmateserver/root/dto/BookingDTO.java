package com.roadmateserver.root.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.roadmateserver.root.common.Constants;
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
public class BookingDTO {
    @JsonProperty("booking_id")
    private Integer id;

    @JsonProperty("renter_id")
    @NotBlank(message = "Renter ID must not be blank")
    private String renterId;

    @JsonProperty("customer_name")
    private String customerName;

    @JsonProperty("vehicle")
    @NotNull(message = "Vehicle must not be null")
    private VehicleDTO vehicle;

    @JsonProperty("start_date")
    @NotBlank(message = "Start date must not be blank")
    private String startDate;

    @JsonProperty("end_date")
    @NotBlank(message = "End date must not be blank")
    private String endDate;

    @JsonProperty("total_price")
    @NotNull(message = "Total price must not be blank")
    private Double totalPrice;

    @JsonProperty("created_at")
    @NotNull(message = "Created at must not be blank")
    private LocalDateTime createdAt;

    @JsonProperty("status")
    @NotNull(message = "Status must not be blank")
    private Constants.BookingStatus status;
}
