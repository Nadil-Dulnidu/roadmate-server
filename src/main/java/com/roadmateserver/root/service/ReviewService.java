package com.roadmateserver.root.service;

import com.roadmateserver.root.dto.ReviewDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewService {

    ReviewDTO createNewReview(ReviewDTO reviewDTO);

    ReviewDTO updateReview(ReviewDTO reviewDTO);

    ReviewDTO getReviewById(Integer reviewId);

    ReviewDTO deleteReview(Integer reviewId);

    List<ReviewDTO> getReviews();

    List<ReviewDTO> getReviewsByVehicleId(Integer vehicleId);

    List<ReviewDTO> getReviewsByUserId(String userId);
}
