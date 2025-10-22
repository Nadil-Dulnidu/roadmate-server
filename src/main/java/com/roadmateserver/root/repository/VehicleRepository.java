package com.roadmateserver.root.repository;

import com.roadmateserver.root.dto.ListingCountProjection;
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

    @Query("""
    SELECT DATE(v.listingDate) AS date, COUNT(v) AS listingCount
    FROM VehicleEntity v
    WHERE v.listingStatus = "APPROVED"
    GROUP BY DATE(v.listingDate)
    ORDER BY DATE(v.listingDate)
    """)
    List<ListingCountProjection> getListingCountByDate();
}
