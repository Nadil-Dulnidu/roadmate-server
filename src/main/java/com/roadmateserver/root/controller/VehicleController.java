package com.roadmateserver.root.controller;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.VehicleDTO;
import com.roadmateserver.root.service.VehicleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/vehicle")
    public ResponseEntity<VehicleDTO> registerVehicle(
            @Valid @RequestBody final VehicleDTO vehicleDTO){
        final VehicleDTO savedVehicleDTO = vehicleService.createNewVehicle(vehicleDTO);
        return ResponseEntity.ok(savedVehicleDTO);
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<VehicleDTO> getVehicleById(
            @Valid
            @Min(value = 1, message = "vehicle id must be a positive number")
            @PathVariable final Integer vehicleId) {
        final VehicleDTO vehicleDTO = vehicleService.getVehicleById(vehicleId);
        return ResponseEntity.ok(vehicleDTO);
    }


    @PutMapping("/vehicle")
    public ResponseEntity<VehicleDTO> updateVehicle(
            @Valid
            @RequestBody final VehicleDTO vehicleDTO) {
        final VehicleDTO updatedVehicleDTO = vehicleService.updateVehicle(vehicleDTO);
        return ResponseEntity.ok(updatedVehicleDTO);
    }


    @DeleteMapping("/vehicle/{vehicleId}")
    public ResponseEntity<VehicleDTO> deleteVehicle(
            @Valid
            @Min(value = 1, message = "vehicle id must be a positive number")
            @PathVariable final Integer vehicleId) {
        final VehicleDTO deletedVehicleDTO = vehicleService.deleteVehicle(vehicleId);
        return ResponseEntity.ok(deletedVehicleDTO);
    }


    @GetMapping("/vehicle")
    public ResponseEntity<Page<VehicleDTO>> getVehicles(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "4") Integer size,
            @RequestParam(value = "vehicleName", required = false) String vehicleName
    ) {
        final Page<VehicleDTO> vehicles = vehicleService.getVehiclesByPage(page,size,vehicleName);
        return ResponseEntity.ok(vehicles);
    }

    @PatchMapping("/vehicle/{vehicleId}")
    public ResponseEntity<VehicleDTO> updateVehicleStatus(
            @Valid
            @Min(value = 1, message = "vehicle id must be a positive number")
            @PathVariable final Integer vehicleId,
            @RequestParam("status") final Constants.VehicleStatus vehicleStatus) {
        final VehicleDTO updatedVehicleDTO = vehicleService.updateVehicleStatus(vehicleId, vehicleStatus);
        return ResponseEntity.ok(updatedVehicleDTO);
    }

    @GetMapping("/vehicle/owner/{ownerId}")
    public ResponseEntity<List<VehicleDTO>> getAllVehiclesByOwnerId(
            @Valid
            @NotBlank(message = "Owner ID must not be blank")
            @PathVariable final String ownerId) {
        final List<VehicleDTO> vehicles = vehicleService.getAllVehiclesByOwnerId(ownerId);
        return ResponseEntity.ok(vehicles);
    }
}
