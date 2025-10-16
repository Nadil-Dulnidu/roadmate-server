package com.roadmateserver.root.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ReviewDTO {

    @JsonProperty("review_id")
    private Integer reviewId;

    @JsonProperty("reviewer_name")
    @NotBlank(message = "Reviewer name must not be blank")
    private String reviewerName;

    @JsonProperty("rating")
    @NotNull(message = "Rating must not be null")
    private Double rating;

    @JsonProperty("comment")
    @NotBlank(message = "Comment must not be blank")
    private String comment;

    @JsonProperty("review_date")
    @NotNull(message = "Review date must not be blank")
    private LocalDateTime reviewDate;

    @JsonProperty("vehicle_id")
    @NotNull(message = "Vehicle ID must not be null")
    private Integer vehicleId;

    @JsonProperty("renter_id")
    @NotBlank(message = "Renter ID must not be null")
    private String renterId;

}
