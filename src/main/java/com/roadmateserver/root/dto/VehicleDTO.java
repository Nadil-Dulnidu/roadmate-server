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

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Data Transfer Object representing a vehicle listing in the system")
public class VehicleDTO {

    @JsonProperty("vehicle_id")
    @Schema(description = "Unique identifier for the vehicle", example = "1")
    private Integer vehicleId;

    @JsonProperty("vehicle_type")
    @NotNull(message = "Vehicle type must not be null")
    @Schema(description = "Type of the vehicle", example = "CAR")
    private Constants.VehicleType vehicleType;

    @JsonProperty("brand")
    @NotBlank(message = "Brand must not be null")
    @Schema(description = "Brand of the vehicle", example = "Toyota")
    private String brand;

    @JsonProperty("model")
    @NotBlank(message = "Model must not be null")
    @Schema(description = "Model of the vehicle", example = "Corolla")
    private String model;

    @JsonProperty("year")
    @NotNull(message = "Year must not be null")
    @Schema(description = "Year of manufacture of the vehicle", example = "2020")
    private Integer year;

    @JsonProperty("color")
    @NotBlank(message = "Color must not be null")
    @Schema(description = "Color of the vehicle", example = "Red")
    private String color;

    @JsonProperty("engine")
    @NotNull(message = "Engine type must not be null")
    @Schema(description = "Engine type of the vehicle", example = "PETROL")
    private Constants.EngineType engine;

    @JsonProperty("transmission")
    @NotNull(message = "Transmission type must not be null")
    @Schema(description = "Transmission type of the vehicle", example = "AUTOMATIC")
    private Constants.TransmissionType transmission;

    @JsonProperty("number_of_seats")
    @NotNull(message = "Number of seats must not be null")
    @Schema(description = "Number of seats in the vehicle", example = "5")
    private Integer numberOfSeats;

    @JsonProperty("license_plate")
    @NotBlank(message = "License plate must not be null")
    @Schema(description = "License plate number of the vehicle", example = "XYZ-1234")
    private String licensePlate;

    @JsonProperty("description")
    @NotBlank(message = "Description must not be blank")
    @Schema(description = "Description of the vehicle", example = "A well-maintained Toyota Corolla with low mileage.")
    private String description;

    @JsonProperty("price_per_day")
    @NotNull(message = "Price per day must not be null")
    @Schema(description = "Price per day for renting the vehicle", example = "50.0")
    private Double pricePerDay;

    @JsonProperty("location")
    @NotBlank(message = "Location must not be blank")
    @Schema(description = "Location where the vehicle is available for rent", example = "123 Main St, Springfield")
    private String location;

    @JsonProperty("city")
    @NotBlank(message = "City must not be blank")
    @Schema(description = "City where the vehicle is located", example = "Springfield")
    private String city;

    @JsonProperty("contact_number")
    @NotBlank(message = "Contact number must not be blank")
    @Schema(description = "Contact number for the vehicle owner", example = "+1234567890")
    private String contactNumber;

    @JsonProperty("rating")
    @Schema(description = "Average rating of the vehicle based on reviews", example = "4.5")
    private Double rating;

    @JsonProperty("review_count")
    @Schema(description = "Number of reviews for the vehicle", example = "10")
    private Integer reviewCount;

    @JsonProperty("available")
    @Schema(description = "Indicates if the vehicle is available for rent", example = "true")
    private Constants.VehicleStatus vehicleStatus = Constants.VehicleStatus.AVAILABLE;

    @JsonProperty("owner_id")
    @NotNull(message = "Owner must not be null")
    @Schema(description = "Unique identifier for the owner of the vehicle", example = "1")
    private String ownerId;

    @JsonProperty("listing_staus")
    private Constants.ListingStatus listingStatus;

    @JsonProperty("images")
    @Schema(description = "List of images associated with the vehicle")
    private List<ImageDTO> images;
}
