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
     * @return a list of {@link BookingEntity} objects associated with the specified renter. Never {@code null}, but may be empty if no bookings are found.
     * @throws IllegalArgumentException if the provided renterId or pageable is {@code null}.
     */
    List<BookingEntity> findAllByRenter_ClerkId(String renterId);
}
