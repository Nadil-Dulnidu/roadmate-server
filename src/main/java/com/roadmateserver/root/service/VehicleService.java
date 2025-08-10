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

    /**
     * Updates an existing vehicle.
     *
     * @param vehicleDTO the vehicle data transfer object containing the updated details of the vehicle.
     * @return the updated vehicle as a VehicleDTO.
     * @throws IllegalArgumentException if the vehicleDTO is null or contains invalid data.
     * @throws VehicleException if the vehicle with the specified ID does not exist.
     */
    VehicleDTO updateVehicle(VehicleDTO vehicleDTO);

    /**
     * Retrieves a vehicle by its unique identifier.
     *
     * @param vehicleId the unique identifier of the vehicle to retrieve.
     * @return the vehicle as a VehicleDTO.
     * @throws IllegalArgumentException if the vehicleId is null or invalid.
     * @throws VehicleException if the vehicle with the specified ID does not exist.
     */
    VehicleDTO getVehicleById(Integer vehicleId);

    /**
     * Deletes a vehicle by its unique identifier.
     *
     * @param vehicleId the unique identifier of the vehicle to delete.
     * @return the deleted vehicle as a VehicleDTO.
     * @throws IllegalArgumentException if the vehicleId is null or invalid.
     * @throws VehicleException if the vehicle with the specified ID does not exist or cannot be deleted.
     */
    VehicleDTO deleteVehicle(Integer vehicleId);

    /**
     * Retrieves all vehicles in the system.
     * @return a list of all vehicles as VehicleDTOs. Never null, but may be empty.
     */
    List<VehicleDTO> getVehicles();

    /**
     * Retrieves all vehicles owned by a specific owner.
     *
     * @param ownerId the unique identifier of the owner whose vehicles are to be retrieved.
     * @return a list of VehicleDTOs representing the vehicles owned by the specified owner. Never null, but may be empty.
     */
    List<VehicleDTO> getAllVehiclesByOwnerId(String ownerId);

    /**
     * Updates the status of a vehicle.
     *
     * @param vehicleId the unique identifier of the vehicle whose status is to be updated.
     * @param vehicleStatus the new status to set for the vehicle.
     * @return the updated VehicleDTO with the new status.
     * @throws IllegalArgumentException if the vehicleId is null or invalid, or if the vehicleStatus is null.
     * @throws VehicleException if the vehicle with the specified ID does not exist or cannot be updated.
     */
    VehicleDTO updateVehicleStatus(Integer vehicleId, Constants.VehicleStatus vehicleStatus);
}
