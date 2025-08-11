package com.roadmateserver.root.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Schema(description = "Data Transfer Object representing a review in the system")
public class ReviewDTO {

    @JsonProperty("review_id")
    private Integer reviewId;

    @JsonProperty("reviewer_name")
    @NotBlank(message = "Reviewer name must not be blank")
    @Schema(description = "Name of the reviewer", example = "John Doe")
    private String reviewerName;

    @JsonProperty("rating")
    @NotNull(message = "Rating must not be null")
    @Schema(description = "Rating given by the reviewer", example = "4.5")
    private Double rating;

    @JsonProperty("comment")
    @NotBlank(message = "Comment must not be blank")
    @Schema(description = "Comment provided by the reviewer", example = "Great experience with this vehicle!")
    private String comment;

    @JsonProperty("review_date")
    @NotNull(message = "Review date must not be blank")
    @Schema(description = "Date when the review was made", example = "2023-10-01")
    private LocalDateTime reviewDate;

    @JsonProperty("vehicle_id")
    @NotNull(message = "Vehicle ID must not be null")
    @Schema(description = "Unique identifier for the vehicle being reviewed", example = "1")
    private Integer vehicleId;

    @JsonProperty("renter_id")
    @NotNull(message = "Renter ID must not be null")
    @Schema(description = "Unique identifier for the renter who made the review", example = "Renter123")
    private String renterId;

}
