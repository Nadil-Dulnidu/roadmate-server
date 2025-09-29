package com.roadmateserver.root.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.roadmateserver.root.common.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleDTO {

    @JsonProperty("vehicle_id")
    private Integer vehicleId;

    @JsonProperty("vehicle_type")
    @NotNull(message = "Vehicle type must not be null")
    private Constants.VehicleType vehicleType;

    @JsonProperty("brand")
    @NotBlank(message = "Brand must not be null")
    private String brand;

    @JsonProperty("model")
    @NotBlank(message = "Model must not be null")
    private String model;

    @JsonProperty("year")
    @NotNull(message = "Year must not be null")
    private Integer year;

    @JsonProperty("color")
    @NotBlank(message = "Color must not be null")
    private String color;

    @JsonProperty("engine")
    @NotNull(message = "Engine type must not be null")
    private Constants.EngineType engine;

    @JsonProperty("transmission")
    @NotNull(message = "Transmission type must not be null")
    private Constants.TransmissionType transmission;

    @JsonProperty("number_of_seats")
    @NotNull(message = "Number of seats must not be null")
    private Integer numberOfSeats;

    @JsonProperty("license_plate")
    @NotBlank(message = "License plate must not be null")
    private String licensePlate;

    @JsonProperty("description")
    @NotBlank(message = "Description must not be blank")
    private String description;

    @JsonProperty("price_per_day")
    @NotNull(message = "Price per day must not be null")
    private Double pricePerDay;

    @JsonProperty("location")
    @NotBlank(message = "Location must not be blank")
    private String location;

    @JsonProperty("city")
    @NotBlank(message = "City must not be blank")
    private String city;

    @JsonProperty("contact_number")
    @NotBlank(message = "Contact number must not be blank")
    private String contactNumber;

    @JsonProperty("rating")
    private Double rating;

    @JsonProperty("review_count")
    private Integer reviewCount;

    @JsonProperty("available")
    @NotNull(message = "Availability must not be null")
    private Constants.VehicleStatus vehicleStatus;

    @JsonProperty("owner_id")
    @NotNull(message = "Owner must not be null")
    private String ownerId;

    @JsonProperty("images")
    @NotNull(message = "Images must not be null")
    private List<ImageDTO> images;

}
