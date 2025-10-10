package com.roadmateserver.root.controller;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.BookingDTO;
import com.roadmateserver.root.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
@Tag(name = "Booking Management", description = "Endpoints for managing bookings and reservations")
@Validated
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Operation(summary = "Create a new booking",
            description = "Create a new booking with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking created successfully",
                    content = @Content(schema = @Schema(implementation = BookingDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid booking data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(produces = Constants.APPLICATION_JSON, consumes = Constants.APPLICATION_JSON)
    public ResponseEntity<BookingDTO> createBooking(
            @Parameter(description = "Booking details to be created", required = true)
            @Valid @RequestBody final BookingDTO bookingDTO) {
        final BookingDTO savedBookingDTO = bookingService.createBooking(bookingDTO);
        return ResponseEntity.ok(savedBookingDTO);
    }

    @Operation(summary = "Get booking by ID",
            description = "Retrieve a booking using its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking found",
                    content = @Content(schema = @Schema(implementation = BookingDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid booking ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<BookingDTO> getBookingById(
            @Parameter(description = "Booking ID (positive integer)", required = true)
            @Valid @Min(value = 1, message = "id must be a positive integer")
            @PathVariable("id") final Integer id) {
        final BookingDTO booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(booking);
    }

    @Operation(summary = "Get all bookings",
            description = "Retrieve a list of all bookings in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bookings retrieved successfully",
                    content = @Content(schema = @Schema(implementation = BookingDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(produces = Constants.APPLICATION_JSON)
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        final List<BookingDTO> bookingDTOS = bookingService.getAllBookings();
        return ResponseEntity.ok(bookingDTOS);
    }

    @Operation(summary = "Delete a booking",
            description = "Delete a booking using its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking deleted successfully",
                    content = @Content(schema = @Schema(implementation = BookingDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid booking ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping(produces = Constants.APPLICATION_JSON, consumes = Constants.APPLICATION_JSON)
    public ResponseEntity<BookingDTO> deleteBooking(
            @Parameter(description = "Booking details to be deleted", required = true)
            @Valid @RequestBody final BookingDTO bookingDTO) {
        final BookingDTO deletedBookingDTO = bookingService.deleteBooking(bookingDTO);
        return ResponseEntity.ok(deletedBookingDTO);
    }

    @Operation(summary = "Update booking status",
            description = "Update the status of a booking using its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking status updated successfully",
                    content = @Content(schema = @Schema(implementation = BookingDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid booking ID or status"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping(value = "/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<BookingDTO> updateBookingStatus(
            @Parameter(description = "Booking ID to update", required = true)
            @Valid @Min(value = 1, message = "booking id must be a positive integer")
            @PathVariable("id") final Integer bookingId,
            @Parameter(description = "New status for the booking", required = true)
            @Valid @RequestParam("status") final Constants.BookingStatus bookingStatus) {
        final BookingDTO updatedBookingDTO = bookingService.updateBookingStatus(bookingId, bookingStatus);
        return ResponseEntity.ok(updatedBookingDTO);
    }

    @Operation(summary = "Get bookings by renter ID",
            description = "Retrieve all bookings made by a specific renter using their unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bookings retrieved successfully",
                    content = @Content(schema = @Schema(implementation = BookingDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid renter ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/renter/{renterId}", produces = Constants.APPLICATION_JSON )
    public ResponseEntity<List<BookingDTO>> getBookingsByRenterId(
            @Parameter(description = "Renter ID to fetch bookings for", required = true)
            @Valid
            @PathVariable("renterId") final String renterId,
            @Parameter(description = "Filter bookings by status")
            @RequestParam(required = false, value = "status") List<Constants.BookingStatus> statuses
    ) {
        final List<BookingDTO> bookings = bookingService.getBookingsByRenterId(renterId, statuses);
        return ResponseEntity.ok(bookings);
    }

    @Operation(summary = "Get bookings by owner ID",
            description = "Retrieve all bookings for vehicles owned by a specific owner using their unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bookings retrieved successfully",
                    content = @Content(schema = @Schema(implementation = BookingDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid owner ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/owner/{ownerId}", produces = Constants.APPLICATION_JSON )
    public ResponseEntity<List<BookingDTO>> getBookingsByOwnerId(
            @Parameter(description = "Owner ID to fetch bookings for", required = true)
            @Valid
            @PathVariable("ownerId") final String ownerId,
            @Parameter(description = "Filter bookings by status")
            @RequestParam(required = false, value = "status") List<Constants.BookingStatus> statuses
    ) {
        final List<BookingDTO> bookings = bookingService.getBookingsByOwnerId(ownerId, statuses);
        return ResponseEntity.ok(bookings);
    }
}
