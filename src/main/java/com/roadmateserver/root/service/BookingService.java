package com.roadmateserver.root.service;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.BookingDTO;
import com.roadmateserver.root.dto.cache.BookingListCache;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {

    /**
     * Creates a new booking in the system.
     * @param bookingDTO the booking data transfer object containing booking details. Must not be {@code null}.
     * @return the created booking data transfer object with any system-generated fields populated (e.g., ID).
     * @throws IllegalArgumentException if the provided bookingDTO is {@code null} or contains invalid data.
     */
    BookingDTO createBooking(BookingDTO bookingDTO);

    /**
     * Retrieves all bookings in the system, optionally filtered by user ID and vehicle ID.
     * @return a list of all booking data transfer objects matching the filters. Never {@code null}, but may be empty.
     */
    BookingListCache getAllBookings(List<Constants.BookingStatus> statuses);

    /**
     * Deletes a booking by its unique identifier.
     * @param bookingDTO the booking data transfer object containing the ID of the booking to delete. Must not be {@code null}.
     * @return the booking data transfer object that was deleted.
     * @throws IllegalArgumentException if the provided bookingId is {@code null} or invalid.
     */
    BookingDTO deleteBooking(Integer bookingId);

    /**
     * Retrieves a booking by its unique identifier.
     * @param bookingId the ID of the booking to retrieve; must not be {@code null}.
     * @return the booking data transfer object with the specified ID.
     * @throws IllegalArgumentException if the provided bookingId is {@code null} or invalid.
     */
    BookingDTO getBookingById(Integer bookingId);

    /**
     * Updates the status of a booking.
     * @param bookingId the ID of the booking to update; must not be {@code null}.
     * @param bookingStatus the new status to set for the booking; must not be {@code null} or empty.
     * @return the updated booking data transfer object after the changes have been persisted.
     * @throws IllegalArgumentException if the provided bookingId is {@code null} or invalid, or if the status is {@code null} or empty.
     */
    BookingDTO updateBookingStatus(Integer bookingId, Constants.BookingStatus bookingStatus);

    /**
     * Retrieves bookings made by a specific renter, optionally filtered by booking status.
     * @param renterId the ID of the renter whose bookings are to be retrieved; must not be {@code null}.
     * @param statuses the list of booking statuses to filter by; can be {@code null} or empty to retrieve all statuses.
     * @return a list of booking data transfer objects matching the criteria.
     * @throws IllegalArgumentException if the provided renterId is {@code null}.
     */
    BookingListCache getBookingsByRenterId(String renterId, List<Constants.BookingStatus> statuses);

    /**
     * Retrieves bookings for vehicles owned by a specific owner, optionally filtered by booking status.
     * @param ownerId the ID of the owner whose vehicle bookings are to be retrieved; must not be {@code null}.
     * @param statuses the list of booking statuses to filter by; can be {@code null} or empty to retrieve all statuses.
     * @return a list of booking data transfer objects matching the criteria.
     * @throws IllegalArgumentException if the provided ownerId is {@code null}.
     */
    BookingListCache getBookingsByOwnerId(String ownerId, List<Constants.BookingStatus> statuses);
}
