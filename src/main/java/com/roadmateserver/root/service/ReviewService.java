package com.roadmateserver.root.service;

import com.roadmateserver.root.dto.ReviewDTO;
import com.roadmateserver.root.dto.cache.ReviewListCache;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewService {

    /**
     * Create a new review.
     *
     * @param reviewDTO the review data transfer object containing review details
     * @return the created ReviewDTO
     */
    ReviewDTO createNewReview(ReviewDTO reviewDTO);

    /**
     * Update an existing review.
     *
     * @param reviewDTO the review data transfer object containing updated review details
     * @return the updated ReviewDTO
     */
    ReviewDTO updateReview(ReviewDTO reviewDTO);

    /**
     * Retrieve a review by its unique identifier.
     *
     * @param reviewId the unique identifier of the review
     * @return the ReviewDTO with the specified ID
     */
    ReviewDTO getReviewById(Integer reviewId);

    /**
     * Delete a review by its unique identifier.
     *
     * @param reviewId the unique identifier of the review to delete
     * @return the deleted ReviewDTO
     */
    ReviewDTO deleteReview(Integer reviewId);

    /**
     * Retrieve all reviews.
     *
     * @return a ReviewListCache containing all reviews
     */
    ReviewListCache getReviews();

    /**
     * Retrieve reviews by vehicle ID.
     *
     * @param vehicleId the unique identifier of the vehicle
     * @return a ReviewListCache containing reviews for the specified vehicle
     */
    ReviewListCache getReviewsByVehicleId(Integer vehicleId);

    /**
     * Retrieve reviews by user ID.
     *
     * @param userId the unique identifier of the user
     * @return a ReviewListCache containing reviews made by the specified user
     */
    ReviewListCache getReviewsByUserId(String userId);
}
