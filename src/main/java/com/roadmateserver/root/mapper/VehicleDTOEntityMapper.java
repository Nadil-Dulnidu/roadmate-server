package com.roadmateserver.root.mapper;

import com.roadmateserver.root.dto.VehicleDTO;
import com.roadmateserver.root.entity.VehicleEntity;

import java.util.Objects;

public class VehicleDTOEntityMapper {
    public static VehicleDTO map(final VehicleEntity vehicleEntity) {
        if (Objects.isNull(vehicleEntity))
            throw new IllegalArgumentException("VehicleDTO must not be null");
        final VehicleDTO dto = new VehicleDTO();
        dto.setVehicleId(vehicleEntity.getVehicleId());
        dto.setBrand(vehicleEntity.getBrand());
        dto.setModel(vehicleEntity.getModel());
        dto.setYear(vehicleEntity.getYear());
        dto.setColor(vehicleEntity.getColor());
        dto.setNumberOfSeats(vehicleEntity.getNumberOfSeats());
        dto.setEngine(vehicleEntity.getEngine());
        dto.setTransmission(vehicleEntity.getTransmission());
        dto.setLicensePlate(vehicleEntity.getLicensePlate());
        dto.setLocation(vehicleEntity.getLocation());
        dto.setCity(vehicleEntity.getCity());
        dto.setDescription(vehicleEntity.getDescription());
        dto.setPricePerDay(vehicleEntity.getPricePerDay());
        dto.setVehicleType(vehicleEntity.getVehicleType());
        dto.setContactNumber(vehicleEntity.getContactNumber());
        dto.setReviewCount(vehicleEntity.getReviewCount());
        dto.setRating(vehicleEntity.getReviewRating());
        dto.setVehicleStatus(vehicleEntity.getIsAvailable());
        dto.setListingStatus(vehicleEntity.getListingStatus());
        dto.setListingDate(vehicleEntity.getListingDate());
        dto.setBasePrice(vehicleEntity.getBasePrice());
        if(Objects.isNull(vehicleEntity.getOwner()))
            throw new IllegalArgumentException("Owner must not be null in VehicleEntity");
        dto.setOwnerId(vehicleEntity.getOwner().getClerkId());
        return dto;
    }

    public static VehicleEntity map(final VehicleDTO vehicleDTO) {
        if (Objects.isNull(vehicleDTO))
            throw new IllegalArgumentException("VehicleEntity must not be null");
        VehicleEntity entity = new VehicleEntity();
        entity.setVehicleId(vehicleDTO.getVehicleId());
        entity.setBrand(vehicleDTO.getBrand());
        entity.setModel(vehicleDTO.getModel());
        entity.setYear(vehicleDTO.getYear());
        entity.setColor(vehicleDTO.getColor());
        entity.setNumberOfSeats(vehicleDTO.getNumberOfSeats());
        entity.setEngine(vehicleDTO.getEngine());
        entity.setTransmission(vehicleDTO.getTransmission());
        entity.setLicensePlate(vehicleDTO.getLicensePlate());
        entity.setLocation(vehicleDTO.getLocation());
        entity.setCity(vehicleDTO.getCity());
        entity.setDescription(vehicleDTO.getDescription());
        entity.setVehicleType(vehicleDTO.getVehicleType());
        entity.setIsAvailable(vehicleDTO.getVehicleStatus());
        entity.setContactNumber(vehicleDTO.getContactNumber());
        entity.setReviewCount(vehicleDTO.getReviewCount());
        entity.setReviewRating(vehicleDTO.getRating());
        entity.setListingStatus(vehicleDTO.getListingStatus());
        entity.setListingDate(vehicleDTO.getListingDate());
        entity.setBasePrice(vehicleDTO.getBasePrice());
        return entity;
    }
}
