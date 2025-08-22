package com.roadmateserver.root.repository;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.entity.BookingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Integer>, JpaSpecificationExecutor<BookingEntity> {

    /**
     * Finds all bookings made by a specific renter.
     * This method retrieves bookings associated with a renter identified by their clerk ID.
     * @param renterId the ID of the renter whose bookings are to be retrieved; must not be {@code null}.
     * @param pageable the pagination information, including page number and size; must not be {@code null}.
     * @return a paginated list of booking entities associated with the specified renter.
     * @throws IllegalArgumentException if the provided renterId or pageable is {@code null}.
     */
    Page<BookingEntity> findAllByRenter_ClerkId(String renterId, Pageable pageable);

    /**
     * Finds all bookings made by a specific renter with a given status.
     * This method retrieves bookings associated with a renter identified by their clerk ID and filters them by status.
     * @param renterId the ID of the renter whose bookings are to be retrieved; must not be {@code null}.
     * @param statuses the list of booking statuses to filter by; must not be {@code null} or empty.
     * @param pageable the pagination information, including page number and size; must not be {@code null}.
     * @return a paginated list of booking entities associated with the specified renter and matching the provided statuses.
     * @throws IllegalArgumentException if the provided renterId, statuses, or pageable is {@code null}.
     */
    Page<BookingEntity> findAllByRenter_ClerkIdAndStatusIn(String renterId, List<Constants.BookingStatus> statuses, Pageable pageable);
}
