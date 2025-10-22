package com.roadmateserver.root.repository;

import com.roadmateserver.root.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Integer>, JpaSpecificationExecutor<BookingEntity> {

    /**
     * Finds all bookings made by a specific renter.
     * This method retrieves bookings associated with a renter identified by their clerk ID.
     * @param renterId the ID of the renter whose bookings are to be retrieved; must not be {@code null}.
     * @return a list of {@link BookingEntity} objects associated with the specified renter. Never {@code null}, but may be empty if no bookings are found.
     * @throws IllegalArgumentException if the provided renterId or pageable is {@code null}.
     */
    List<BookingEntity> findAllByRenter_ClerkId(String renterId);


    List<BookingEntity> findAllByVehicle_Owner_ClerkId(String ownerId);

    /**
     * Retrieves the total revenue generated for each vehicle.
     * The revenue is calculated as the sum of (totalPrice - basePrice) for all bookings associated with each vehicle.
     * @return a list of maps where each map contains 'vehicleId' and its corresponding 'totalRevenue'.
     */
    @Query("""
        SELECT COALESCE(SUM(b.totalPrice - b.vehicle.basePrice), 0)
        FROM BookingEntity b
        WHERE b.vehicle.vehicleId = :vehicleId
    """)
    Double getTotalRevenueByVehicleId(@Param("vehicleId") Integer vehicleId);

    /**
     * Retrieves the overall total revenue generated from all bookings.
     * The revenue is calculated as the sum of (totalPrice - basePrice) for all bookings.
     * @return the total revenue as a Double. If there are no bookings, returns 0.
     */
    @Query("""
    SELECT COALESCE(SUM(b.totalPrice - b.vehicle.basePrice), 0)
    FROM BookingEntity b
    WHERE b.status = 'COMPLETED'
    """)
    Double getTotalRevenue();
}
