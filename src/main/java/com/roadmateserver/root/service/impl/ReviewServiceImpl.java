package com.roadmateserver.root.service.impl;

import com.roadmateserver.root.dto.ReviewDTO;
import com.roadmateserver.root.dto.cache.ReviewListCache;
import com.roadmateserver.root.entity.ReviewEntity;
import com.roadmateserver.root.entity.UserEntity;
import com.roadmateserver.root.entity.VehicleEntity;
import com.roadmateserver.root.exception.ReviewNotFoundException;
import com.roadmateserver.root.exception.UserNotFoundException;
import com.roadmateserver.root.exception.VehicleNotFoundException;
import com.roadmateserver.root.mapper.ReviewDTOEntityMapper;
import com.roadmateserver.root.repository.ReviewRepository;
import com.roadmateserver.root.repository.UserRepository;
import com.roadmateserver.root.repository.VehicleRepository;
import com.roadmateserver.root.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository, VehicleRepository vehicleRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {"reviewListCache", "reviewCache"}, allEntries = true)
    public ReviewDTO createNewReview(final ReviewDTO reviewDTO) {
        if(Objects.isNull(reviewDTO)){
            log.error("ReviewDTO cannot be null");
            throw new IllegalArgumentException("ReviewDTO cannot be null");
        }
        log.info("Creating new review for vehicle ID: {}", reviewDTO.getVehicleId());
        final VehicleEntity vehicleEntity = vehicleRepository.findById(reviewDTO.getVehicleId())
                .orElseThrow(() -> {
                    log.error("Vehicle with ID {} not found", reviewDTO.getVehicleId());
                    return new VehicleNotFoundException("Vehicle not found");
                });
        log.debug("Vehicle found: {}", vehicleEntity);
        final UserEntity userEntity = userRepository.findByClerkId(reviewDTO.getRenterId())
                .orElseThrow(() -> {
                    log.error("User with ID {} not found", reviewDTO.getRenterId());
                    return new UserNotFoundException("User not found");
                });
        log.debug("User found: {}", userEntity);
        final ReviewEntity reviewEntity = ReviewDTOEntityMapper.map(reviewDTO);
        reviewEntity.setVehicle(vehicleEntity);
        reviewEntity.setUser(userEntity);
        log.debug("Review entity created: {}", reviewEntity);
        final ReviewEntity savedReviewEntity = reviewRepository.save(reviewEntity);
        final VehicleEntity updatedVehicle = setReviewCountAndRating(vehicleEntity);
        vehicleRepository.save(updatedVehicle);
        log.debug("Vehicle updated with new review count and rating: {}", updatedVehicle);
        log.info("Review created successfully with ID: {}", savedReviewEntity.getReviewId());
        return ReviewDTOEntityMapper.map(savedReviewEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {"reviewListCache"}, allEntries = true)
    @CachePut(value = "reviewCache", key = "'review_' + #reviewDTO.reviewId")
    public ReviewDTO updateReview(final ReviewDTO reviewDTO) {
        if(Objects.isNull(reviewDTO)){
            log.error("ReviewDTO cannot be null");
            throw new IllegalArgumentException("ReviewDTO cannot be null");
        }
        log.info("Updating review with ID: {}", reviewDTO.getReviewId());
        final ReviewEntity existingReview = reviewRepository.findById(reviewDTO.getReviewId())
                .orElseThrow(() -> {
                    log.error("Review with ID {} not found", reviewDTO.getReviewId());
                    return new ReviewNotFoundException("Review not found");
                });
        log.debug("Existing review found: {}", existingReview);
        existingReview.setReviewerName(reviewDTO.getReviewerName());
        existingReview.setRating(reviewDTO.getRating());
        existingReview.setReviewText(reviewDTO.getComment());
        log.debug("Review entity updated: {}", existingReview);
        final ReviewEntity updatedReviewEntity = reviewRepository.save(existingReview);
        log.info("Review updated successfully with ID: {}", updatedReviewEntity.getReviewId());
        return ReviewDTOEntityMapper.map(updatedReviewEntity);
    }

    @Override
    @Cacheable(value = "reviewCache", key = "'review_' + #reviewId")
    public ReviewDTO getReviewById(final Integer reviewId) {
        if(Objects.isNull(reviewId)){
            log.error("Review ID cannot be null");
            throw new IllegalArgumentException("Review ID cannot be null");
        }
        log.info("Retrieving review with ID: {}", reviewId);
        final ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> {
                    log.error("Review with ID {} not found", reviewId);
                    return new IllegalArgumentException("Review not found");
                });
        log.debug("Review entity found: {}", reviewEntity);
        return ReviewDTOEntityMapper.map(reviewEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {"reviewListCache", "reviewCache"}, key = "'review_'+ #reviewId" , allEntries = true)
    public ReviewDTO deleteReview(final Integer reviewId) {
        if(Objects.isNull(reviewId)){
            log.error("Review ID cannot be null");
            throw new IllegalArgumentException("Review ID cannot be null");
        }
        log.info("Deleting review with ID: {}", reviewId);
        final ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> {
                    log.error("Review with ID {} not found", reviewId);
                    return new IllegalArgumentException("Review not found");
                });
        log.debug("Review entity found for deletion: {}", reviewEntity);
        reviewRepository.delete(reviewEntity);
        log.info("Review deleted successfully with ID: {}", reviewId);
        return ReviewDTOEntityMapper.map(reviewEntity);
    }

    @Override
    @Cacheable(value = "reviewListCache", key = "'all_reviews'")
    public ReviewListCache getReviews() {
        log.info("Retrieving reviews");
        final List<ReviewDTO> reviewDTOS = reviewRepository.findAll()
                .stream()
                .map(ReviewDTOEntityMapper::map)
                .toList();
        log.debug("Reviews retrieved: {}", reviewDTOS);
        return new ReviewListCache(reviewDTOS);
    }

    @Override
    @Cacheable(value = "reviewListCache", key = "'vehicle_reviews_' + #vehicleId")
    public ReviewListCache getReviewsByVehicleId(final Integer vehicleId) {
        if(Objects.isNull(vehicleId)){
            log.error("Vehicle ID cannot be null");
            throw new IllegalArgumentException("Vehicle ID cannot be null");
        }
        log.info("Retrieving reviews for vehicle ID: {}", vehicleId);
        final List<ReviewDTO> reviewDTOS = reviewRepository.findAllByVehicle_VehicleId(vehicleId)
                .stream()
                .map(ReviewDTOEntityMapper::map)
                .toList();
        log.debug("Reviews for vehicle ID {} retrieved: {}", vehicleId, reviewDTOS);
        return new ReviewListCache(reviewDTOS);
    }

    @Override
    @Cacheable(value = "reviewListCache", key = "'user_reviews_' + #userId")
    public ReviewListCache getReviewsByUserId(final String userId) {
        if(Objects.isNull(userId)){
            log.error("User ID cannot be null");
            throw new IllegalArgumentException("User ID cannot be null");
        }
        log.info("Retrieving reviews for user ID: {}", userId);
        final List<ReviewDTO> reviewDTOS = reviewRepository.findAllByUser_ClerkId(userId)
                .stream()
                .map(ReviewDTOEntityMapper::map)
                .toList();
        log.debug("Reviews for user ID {} retrieved: {}", userId, reviewDTOS);
        return new ReviewListCache(reviewDTOS);
    }

    //helper method to set review count and rating
    public VehicleEntity setReviewCountAndRating(VehicleEntity vehicleEntity) {
        if (Objects.isNull(vehicleEntity)) {
            log.error("Vehicle entity cannot be null");
            throw new IllegalArgumentException("Vehicle entity cannot be null");
        }
        log.info("Setting review count and rating for vehicle ID: {}", vehicleEntity.getVehicleId());
        List<ReviewEntity> reviews = reviewRepository.findAllByVehicle_VehicleId(vehicleEntity.getVehicleId());
        if (reviews.isEmpty()) {
            vehicleEntity.setReviewCount(0);
            vehicleEntity.setReviewRating(0.0);
        } else {
            double totalRating = reviews.stream().mapToDouble(ReviewEntity::getRating).sum();
            vehicleEntity.setReviewCount(reviews.size());
            vehicleEntity.setReviewRating(totalRating / reviews.size());
        }
        log.debug("Review count and rating set for vehicle ID {}: Count = {}, Rating = {}",
                  vehicleEntity.getVehicleId(),
                  vehicleEntity.getReviewCount(),
                  vehicleEntity.getReviewRating());
        return vehicleEntity;
    }
}
