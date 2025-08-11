package com.roadmateserver.root.repository;

import com.roadmateserver.root.entity.ReviewEntity;
import com.roadmateserver.root.entity.UserEntity;
import com.roadmateserver.root.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {

    /**
     * Finds all reviews for a specific vehicle.
     *
     * @param vehicle the vehicle for which to find reviews
     * @return a list of reviews for the specified vehicle
     */
    List<ReviewEntity> findAllByVehicle_VehicleId(Integer vehicleId);

    /**
     * Finds all reviews made by a specific user.
     *
     * @param user the user for whom to find reviews
     * @return a list of reviews made by the specified user
     */
    List<ReviewEntity> findAllByUser_ClerkId(String userId);
}
