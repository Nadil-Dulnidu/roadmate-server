package com.roadmateserver.root.mapper;

import com.roadmateserver.root.dto.ReviewDTO;
import com.roadmateserver.root.entity.ReviewEntity;

import java.util.Objects;

public class ReviewDTOEntityMapper {

    public static ReviewEntity map(ReviewDTO dto) {
        if (Objects.isNull(dto))
            throw new IllegalArgumentException("ReviewDTO must not be null");
        ReviewEntity entity = new ReviewEntity();
        entity.setRating(dto.getRating());
        entity.setReviewText(dto.getComment());
        entity.setCreatedAt(dto.getReviewDate());
        entity.setReviewerName(dto.getReviewerName());
        return entity;
    }

    public static ReviewDTO map(ReviewEntity entity) {
        if (Objects.isNull(entity))
            throw new IllegalArgumentException("ReviewEntity must not be null");
        ReviewDTO dto = new ReviewDTO();
        dto.setReviewId(entity.getReviewId());
        dto.setRating(entity.getRating());
        dto.setComment(entity.getReviewText());
        dto.setReviewDate(entity.getCreatedAt());
        dto.setReviewerName(entity.getReviewerName());
        if(Objects.isNull(entity.getUser()))
            throw new IllegalArgumentException("Reviewer must not be null in ReviewEntity");
        dto.setRenterId(entity.getUser().getClerkId());
        if(Objects.isNull(entity.getVehicle()))
            throw new IllegalArgumentException("Vehicle must not be null in ReviewEntity");
        dto.setVehicleId(entity.getVehicle().getVehicleId());
        return dto;
    }
}
