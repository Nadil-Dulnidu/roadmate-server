package com.roadmateserver.root.service;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.BookingDTO;
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
     * @param userIdFilter list of user IDs to filter bookings by, can be empty or null for no filtering.
     * @param vehicleIdFilter list of vehicle IDs to filter bookings by, can be empty or null for no filtering.
     * @return a list of all booking data transfer objects matching the filters. Never {@code null}, but may be empty.
     */
    List<BookingDTO> getAllBookings();

    /**
     * Deletes a booking by its unique identifier.
     * @param bookingId the ID of the booking to delete; must not be {@code null}.
     * @return the booking data transfer object that was deleted.
     * @throws IllegalArgumentException if the provided bookingId is {@code null} or invalid.
     */
    BookingDTO deleteBooking(BookingDTO bookingDTO);

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
     * @param status the new status to set for the booking; must not be {@code null} or empty.
     * @return the updated booking data transfer object after the changes have been persisted.
     * @throws IllegalArgumentException if the provided bookingId is {@code null} or invalid, or if the status is {@code null} or empty.
     */
    BookingDTO updateBookingStatus(Integer bookingId, Constants.BookingStatus bookingStatus);
}
