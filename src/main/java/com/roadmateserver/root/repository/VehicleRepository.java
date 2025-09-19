package com.roadmateserver.root.repository;

import com.roadmateserver.root.entity.UserEntity;
import com.roadmateserver.root.entity.VehicleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Integer> {

    /**
     * Finds a vehicle by its license plate.
     *
     * @param licensePlate the license plate of the vehicle
     * @return an Optional containing the VehicleEntity if found, or empty if not found
     */
    Optional<VehicleEntity> findByLicensePlate(String licensePlate);

    /**
     * Finds all vehicles owned by a specific user.
     *
     * @param owner the owner of the vehicles
     * @return a list of VehicleEntity objects owned by the specified user
     */
    List<VehicleEntity> findByOwner(UserEntity owner);


    /**
     * Finds all vehicles with optional filtering by model name and pagination support.
     *
     * @param name     the model name to filter by (case-insensitive, partial match); if null or empty, no filtering is applied
     * @param pageable the pagination information
     * @return a page of VehicleEntity objects matching the criteria
     */
    @Query("SELECT v FROM VehicleEntity v WHERE (:name IS NULL OR :name = '' OR LOWER(v.model) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<VehicleEntity> findAll(String name, Pageable pageable);
}
