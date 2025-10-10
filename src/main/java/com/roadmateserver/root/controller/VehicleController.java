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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/listing")
@Validated
@Slf4j
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
    @PostMapping(value = "/vehicle", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VehicleDTO> registerVehicle(
            @Parameter(description = "The image files for the vehicle.", required = true)
            @RequestPart("files") final List<MultipartFile> files,
            @Parameter(description = "Vehicle details in JSON format.", required = true)
            @RequestPart("vehicle") @Valid final VehicleDTO vehicleDTO){
        final VehicleDTO savedVehicleDTO = vehicleService.createNewVehicle(vehicleDTO, files);
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
    public ResponseEntity<Page<VehicleDTO>> getVehicles(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "4") Integer size,
            @RequestParam(value = "vehicleName", required = false) String vehicleName
    ) {
        final Page<VehicleDTO> vehicles = vehicleService.getVehiclesByPage(page,size,vehicleName);
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
            @Min(value = 1, message = "vehicle id must be a positive number")
            @PathVariable final Integer vehicleId,
            @RequestParam("status") final Constants.VehicleStatus vehicleStatus) {
        log.info("Updating vehicle ID {} to status {}", vehicleId, vehicleStatus);
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
