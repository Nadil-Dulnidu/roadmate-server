package com.roadmateserver.root.service;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.VehicleDTO;
import org.springframework.stereotype.Service;
import com.roadmateserver.root.exception.VehicleException;

import java.util.List;

@Service
public interface VehicleService {

    /**
     * Creates a new vehicle.
     *
     * @param vehicleDTO the vehicle data transfer object containing the details of the vehicle to be created.
     * @return the created vehicle as a VehicleDTO.
     * @throws IllegalArgumentException if the vehicleDTO is null or contains invalid data.
     * @throws VehicleException if a vehicle with the same license plate already exists.
     */
    VehicleDTO createNewVehicle(VehicleDTO vehicleDTO);

    VehicleDTO updateVehicle(VehicleDTO vehicleDTO);

    VehicleDTO getVehicleById(Integer vehicleId);

    VehicleDTO deleteVehicle(Integer vehicleId);

    List<VehicleDTO> getVehicles();

    List<VehicleDTO> getAllVehiclesByOwnerId(String ownerId);

    VehicleDTO updateVehicleStatus(Integer vehicleId, Constants.VehicleStatus vehicleStatus);
}
