package com.roadmateserver.root.controller;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.VehicleDTO;
import com.roadmateserver.root.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/listing")
@Validated
@Tag(name = "Vehicle Listing Management", description = "Endpoints for managing vehicle listings")
public class VehicleController {
    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @Operation(summary = "Register a new vehicle",
            description = "Create a new vehicle listing with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle registered successfully",
                    content = @Content(schema = @Schema(implementation = VehicleDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid vehicle data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/vehicle")
    public ResponseEntity<VehicleDTO> registerVehicle(
            @Parameter(description = "Vehicle details to be registered",
                    required = true)
            @Valid @RequestBody final VehicleDTO vehicleDTO){
        final VehicleDTO savedVehicleDTO = vehicleService.createNewVehicle(vehicleDTO);
        return ResponseEntity.ok(savedVehicleDTO);
    }

    @Operation(summary = "Get vehicle by ID",
            description = "Retrieve a vehicle using its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle found",
                    content = @Content(schema = @Schema(implementation = VehicleDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid vehicle ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<VehicleDTO> getVehicleById(
            @Parameter(description = "Vehicle ID (positive integer)", required = true)
            @Valid
            @Min(value = 1, message = "vehicle id must be a positive number")
            @PathVariable final Integer vehicleId) {
        final VehicleDTO vehicleDTO = vehicleService.getVehicleById(vehicleId);
        return ResponseEntity.ok(vehicleDTO);
    }

    @Operation(summary = "Update vehicle details",
            description = "Update the details of an existing vehicle listing.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle updated successfully",
                    content = @Content(schema = @Schema(implementation = VehicleDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid vehicle data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/vehicle")
    public ResponseEntity<VehicleDTO> updateVehicle(
            @Parameter(description = "Vehicle details to be updated",
                    required = true)
            @Valid
            @RequestBody final VehicleDTO vehicleDTO) {
        final VehicleDTO updatedVehicleDTO = vehicleService.updateVehicle(vehicleDTO);
        return ResponseEntity.ok(updatedVehicleDTO);
    }

    @Operation(summary = "Delete a vehicle",
            description = "Remove a vehicle listing using its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle deleted successfully",
                    content = @Content(schema = @Schema(implementation = VehicleDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid vehicle ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/vehicle/{vehicleId}")
    public ResponseEntity<VehicleDTO> deleteVehicle(
            @Parameter(description = "Vehicle ID (positive integer)", required = true)
            @Valid
            @Min(value = 1, message = "vehicle id must be a positive number")
            @PathVariable final Integer vehicleId) {
        final VehicleDTO deletedVehicleDTO = vehicleService.deleteVehicle(vehicleId);
        return ResponseEntity.ok(deletedVehicleDTO);
    }

    @Operation(summary = "Get all vehicles",
            description = "Retrieve a list of all vehicle listings.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicles retrieved successfully",
                    content = @Content(schema = @Schema(implementation = VehicleDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/vehicle")
    public ResponseEntity<List<VehicleDTO>> getVehicles() {
        final List<VehicleDTO> vehicles = vehicleService.getVehicles();
        return ResponseEntity.ok(vehicles);
    }

    @Operation(summary = "Update vehicle status",
            description = "Change the status of a vehicle listing (e.g., available, unavailable).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle status updated successfully",
                    content = @Content(schema = @Schema(implementation = VehicleDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid vehicle ID or status"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/vehicle/{vehicleId}")
    public ResponseEntity<VehicleDTO> updateVehicleStatus(
            @Parameter(description = "Vehicle ID (positive integer)", required = true)
            @Valid
            @Min(value = 1, message = "vehicle id must be a positive number")
            @PathVariable final Integer vehicleId,
            @RequestParam final Constants.VehicleStatus vehicleStatus) {
        final VehicleDTO updatedVehicleDTO = vehicleService.updateVehicleStatus(vehicleId, vehicleStatus);
        return ResponseEntity.ok(updatedVehicleDTO);
    }

    @Operation(summary = "Get all vehicles by owner ID",
            description = "Retrieve a list of all vehicle listings for a specific owner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicles retrieved successfully",
                    content = @Content(schema = @Schema(implementation = VehicleDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid owner ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/vehicle/owner/{ownerId}")
    public ResponseEntity<List<VehicleDTO>> getAllVehiclesByOwnerId(
            @Parameter(description = "Owner ID (non-blank string)", required = true)
            @Valid
            @NotBlank(message = "Owner ID must not be blank")
            @PathVariable final String ownerId) {
        final List<VehicleDTO> vehicles = vehicleService.getAllVehiclesByOwnerId(ownerId);
        return ResponseEntity.ok(vehicles);
    }
}
