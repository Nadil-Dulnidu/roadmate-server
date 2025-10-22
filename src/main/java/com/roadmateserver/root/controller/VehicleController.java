package com.roadmateserver.root.controller;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.VehicleDTO;
import com.roadmateserver.root.service.VehicleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/listing")
@Validated
public class VehicleController {
    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PreAuthorize(Constants.OWNER_ROLE_PERMISSION)
    @PostMapping(value = "/vehicle", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VehicleDTO> registerVehicle(
            @RequestPart("files") final List<MultipartFile> files,
            @RequestPart("vehicle") @Valid final VehicleDTO vehicleDTO){
        final VehicleDTO savedVehicleDTO = vehicleService.createNewVehicle(vehicleDTO, files);
        return ResponseEntity.ok(savedVehicleDTO);
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<VehicleDTO> getVehicleById(
            @Min(value = 1, message = "vehicle id must be a positive number")
            @PathVariable final Integer vehicleId) {
        final VehicleDTO vehicleDTO = vehicleService.getVehicleById(vehicleId);
        return ResponseEntity.ok(vehicleDTO);
    }

    @PreAuthorize(Constants.OWNER_ROLE_PERMISSION)
    @PutMapping("/vehicle")
    public ResponseEntity<VehicleDTO> updateVehicle(
            @Valid
            @RequestBody final VehicleDTO vehicleDTO) {
        final VehicleDTO updatedVehicleDTO = vehicleService.updateVehicle(vehicleDTO);
        return ResponseEntity.ok(updatedVehicleDTO);
    }

    @PreAuthorize(Constants.OWNER_ROLE_PERMISSION)
    @DeleteMapping("/vehicle/{vehicleId}")
    public ResponseEntity<VehicleDTO> deleteVehicle(
            @Min(value = 1, message = "vehicle id must be a positive number")
            @PathVariable final Integer vehicleId) {
        final VehicleDTO deletedVehicleDTO = vehicleService.deleteVehicle(vehicleId);
        return ResponseEntity.ok(deletedVehicleDTO);
    }

    @GetMapping("/vehicle")
    public ResponseEntity<List<VehicleDTO>> getAllVehicles(
            @RequestParam(value = "listingStatus", required = false) final
            List<Constants.ListingStatus> statuses,
            @RequestParam(value = "vehicleStatus", required = false) final
            List<Constants.VehicleStatus> vehicleStatuses) {
        final List<VehicleDTO> vehicles = vehicleService.getAllVehicles(statuses, vehicleStatuses);
        return ResponseEntity.ok(vehicles);
    }

    @PreAuthorize(Constants.OWNER_ROLE_PERMISSION)
    @PatchMapping("/vehicle/{vehicleId}")
    public ResponseEntity<VehicleDTO> updateVehicleStatus(
            @Min(value = 1, message = "vehicle id must be a positive number")
            @PathVariable final Integer vehicleId,
            @RequestParam("status") final Constants.VehicleStatus vehicleStatus) {
        final VehicleDTO updatedVehicleDTO = vehicleService.updateVehicleStatus(vehicleId, vehicleStatus);
        return ResponseEntity.ok(updatedVehicleDTO);
    }

    @GetMapping("/vehicle/owner/{ownerId}")
    public ResponseEntity<List<VehicleDTO>> getAllVehiclesByOwnerId(
            @PathVariable final String ownerId) {
        final List<VehicleDTO> vehicles = vehicleService.getAllVehiclesByOwnerId(ownerId);
        return ResponseEntity.ok(vehicles);
    }

    @PreAuthorize(Constants.ADMIN_OR_STAFF_ROLE_PERMISSION)
    @PatchMapping("/vehicle/listing-status/{vehicleId}")
    public ResponseEntity<VehicleDTO> updateListingStatus(
            @Min(value = 1, message = "vehicle id must be a positive number")
            @PathVariable final Integer vehicleId,
            @RequestParam("listingStatus") final Constants.ListingStatus listingStatus) {
        final VehicleDTO updatedVehicleDTO = vehicleService.updateListingStatus(vehicleId, listingStatus);
        return ResponseEntity.ok(updatedVehicleDTO);
    }
}
