package com.roadmateserver.root.controller;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.BookingDTO;
import com.roadmateserver.root.dto.ReviewDTO;
import com.roadmateserver.root.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
@Validated
@Tag(name = "Review Management", description = "Endpoints for managing reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "Create a new review",
               description = "Creates a new review for a vehicle by a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review created successfully",
                    content = @Content(schema = @Schema(implementation = BookingDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid review data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(consumes = Constants.APPLICATION_JSON, produces = Constants.APPLICATION_JSON)
    public ResponseEntity<ReviewDTO> createReview(
            @Parameter(description = "Review details to be created", required = true)
            @Valid
            @RequestBody final ReviewDTO reviewDTO) {
        final ReviewDTO createdReview = reviewService.createNewReview(reviewDTO);
        return ResponseEntity.ok(createdReview);
    }

    @Operation(summary = "Get reviews for a vehicle",
               description = "Retrieves all reviews for a specific vehicle.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ReviewDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid vehicle ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/vehicle/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<List<ReviewDTO>> getReviewsForVehicle(
            @Parameter(description = "Vehicle ID (positive integer)", required = true)
            @Valid
            @Min(value = 1, message = "id must be a positive integer")
            @PathVariable("id") final Integer vehicleId) {
        final List<ReviewDTO> reviews = reviewService.getReviewsByVehicleId(vehicleId);
        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "Get reviews by user ID",
               description = "Retrieves all reviews made by a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ReviewDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/user/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<List<ReviewDTO>> getReviewsByUserId(
            @Parameter(description = "User ID (Clerk ID)", required = true)
            @Valid
            @PathVariable("id") final String userId) {
        final List<ReviewDTO> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "Get all reviews",
               description = "Retrieves all reviews in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ReviewDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(produces = Constants.APPLICATION_JSON)
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        final List<ReviewDTO> reviews = reviewService.getReviews();
        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "Delete a review",
               description = "Deletes a review by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review deleted successfully",
                    content = @Content(schema = @Schema(implementation = ReviewDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid review ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping(value = "/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<ReviewDTO> deleteReview(
            @Parameter(description = "Review ID (positive integer)", required = true)
            @Valid
            @Min(value = 1 , message = "id must be positive integer")
            @PathVariable("id") final Integer reviewId) {
        ReviewDTO deletedReview = reviewService.deleteReview(reviewId);
        return ResponseEntity.ok(deletedReview);
    }

    @Operation(summary = "Update a review",
               description = "Updates an existing review by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review updated successfully",
                    content = @Content(schema = @Schema(implementation = ReviewDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid review data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(consumes = Constants.APPLICATION_JSON, produces = Constants.APPLICATION_JSON)
    public ResponseEntity<ReviewDTO> updateReview(
            @Parameter(description = "Review details to be updated", required = true)
            @Valid
            @RequestBody final ReviewDTO reviewDTO) {
        final ReviewDTO updatedReview = reviewService.updateReview(reviewDTO);
        return ResponseEntity.ok(updatedReview);
    }

    @Operation(summary = "Get review by ID",
               description = "Retrieves a review by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review found",
                    content = @Content(schema = @Schema(implementation = ReviewDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid review ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<ReviewDTO> getReviewById(
            @Parameter(description = "Review ID (positive integer)", required = true)
            @Valid
            @Min(value = 1, message = "id must be a positive integer")
            @PathVariable("id") final Integer id) {
        final ReviewDTO review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }
}


