package com.roadmateserver.root.controller;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.BookingDTO;
import com.roadmateserver.root.service.BookingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
@Validated
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PreAuthorize(Constants.RENTER_ROLE_PERMISSION)
    @PostMapping(produces = Constants.APPLICATION_JSON, consumes = Constants.APPLICATION_JSON)
    public ResponseEntity<BookingDTO> createBooking(
            @Valid @RequestBody final BookingDTO bookingDTO) {
        final BookingDTO savedBookingDTO = bookingService.createBooking(bookingDTO);
        return ResponseEntity.ok(savedBookingDTO);
    }

    @PreAuthorize(Constants.RENTER_ROLE_PERMISSION)
    @GetMapping(value = "/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<BookingDTO> getBookingById(
            @Min(value = 1, message = "id must be a positive integer")
            @PathVariable("id") final Integer id) {
        final BookingDTO booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(booking);
    }

    @PreAuthorize(Constants.ADMIN_OR_STAFF_ROLE_PERMISSION)
    @GetMapping(produces = Constants.APPLICATION_JSON)
    public ResponseEntity<List<BookingDTO>> getAllBookings(
            @RequestParam(required = false, value = "status")
            List<Constants.BookingStatus> statuses) {
        final List<BookingDTO> bookingDTOS = bookingService.getAllBookings(statuses);
        return ResponseEntity.ok(bookingDTOS);
    }

    @DeleteMapping(value = "/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<BookingDTO> deleteBooking(
            @PathVariable Integer id) {
        final BookingDTO deletedBookingDTO = bookingService.deleteBooking(id);
        return ResponseEntity.ok(deletedBookingDTO);
    }

    @PatchMapping(value = "/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<BookingDTO> updateBookingStatus(
            @Valid @Min(value = 1, message = "booking id must be a positive integer")
            @PathVariable("id") final Integer bookingId,
            @Valid @RequestParam("status") final Constants.BookingStatus bookingStatus) {
        final BookingDTO updatedBookingDTO = bookingService.updateBookingStatus(bookingId, bookingStatus);
        return ResponseEntity.ok(updatedBookingDTO);
    }

    @GetMapping(value = "/renter/{renterId}", produces = Constants.APPLICATION_JSON )
    public ResponseEntity<List<BookingDTO>> getBookingsByRenterId(
            @PathVariable("renterId") final String renterId,
            @RequestParam(required = false, value = "status") List<Constants.BookingStatus> statuses) {
        final List<BookingDTO> bookings = bookingService.getBookingsByRenterId(renterId, statuses);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping(value = "/owner/{ownerId}", produces = Constants.APPLICATION_JSON )
    public ResponseEntity<List<BookingDTO>> getBookingsByOwnerId(
            @PathVariable("ownerId") final String ownerId,
            @RequestParam(required = false, value = "status") List<Constants.BookingStatus> statuses) {
        final List<BookingDTO> bookings = bookingService.getBookingsByOwnerId(ownerId, statuses);
        return ResponseEntity.ok(bookings);
    }
}
