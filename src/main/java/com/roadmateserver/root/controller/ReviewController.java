package com.roadmateserver.root.controller;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.ReviewDTO;
import com.roadmateserver.root.service.ReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
@Validated
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PreAuthorize(Constants.RENTER_ROLE_PERMISSION)
    @PostMapping(consumes = Constants.APPLICATION_JSON, produces = Constants.APPLICATION_JSON)
    public ResponseEntity<ReviewDTO> createReview(
            @Valid
            @RequestBody final ReviewDTO reviewDTO) {
        final ReviewDTO createdReview = reviewService.createNewReview(reviewDTO);
        return ResponseEntity.ok(createdReview);
    }

    @PreAuthorize(Constants.OWNER_ROLE_PERMISSION)
    @GetMapping(value = "/vehicle/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<List<ReviewDTO>> getReviewsForVehicle(
            @Min(value = 1, message = "id must be a positive integer")
            @PathVariable("id") final Integer vehicleId) {
        final List<ReviewDTO> reviews = reviewService.getReviewsByVehicleId(vehicleId);
        return ResponseEntity.ok(reviews);
    }

    @PreAuthorize(Constants.RENTER_ROLE_PERMISSION)
    @GetMapping(value = "/user/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<List<ReviewDTO>> getReviewsByUserId(
            @PathVariable("id") final String userId) {
        final List<ReviewDTO> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping(produces = Constants.APPLICATION_JSON)
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        final List<ReviewDTO> reviews = reviewService.getReviews();
        return ResponseEntity.ok(reviews);
    }

    @PreAuthorize(Constants.RENTER_ROLE_PERMISSION)
    @DeleteMapping(value = "/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<ReviewDTO> deleteReview(
            @Min(value = 1 , message = "id must be positive integer")
            @PathVariable("id") final Integer reviewId) {
        ReviewDTO deletedReview = reviewService.deleteReview(reviewId);
        return ResponseEntity.ok(deletedReview);
    }

    @PreAuthorize(Constants.RENTER_ROLE_PERMISSION)
    @PutMapping(consumes = Constants.APPLICATION_JSON, produces = Constants.APPLICATION_JSON)
    public ResponseEntity<ReviewDTO> updateReview(
            @Valid
            @RequestBody final ReviewDTO reviewDTO) {
        final ReviewDTO updatedReview = reviewService.updateReview(reviewDTO);
        return ResponseEntity.ok(updatedReview);
    }

    @GetMapping(value = "/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<ReviewDTO> getReviewById(
            @Min(value = 1, message = "id must be a positive integer")
            @PathVariable("id") final Integer id) {
        final ReviewDTO review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }
}


