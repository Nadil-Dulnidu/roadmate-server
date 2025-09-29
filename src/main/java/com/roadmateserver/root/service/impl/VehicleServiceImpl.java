package com.roadmateserver.root.service.impl;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.ImageDTO;
import com.roadmateserver.root.dto.VehicleDTO;
import com.roadmateserver.root.entity.ImageEntity;
import com.roadmateserver.root.entity.UserEntity;
import com.roadmateserver.root.entity.VehicleEntity;
import com.roadmateserver.root.exception.VehicleException;
import com.roadmateserver.root.exception.VehicleNotFoundException;
import com.roadmateserver.root.mapper.ImageDTOEntityMapper;
import com.roadmateserver.root.mapper.VehicleDTOEntityMapper;
import com.roadmateserver.root.repository.ImageRepository;
import com.roadmateserver.root.repository.UserRepository;
import com.roadmateserver.root.repository.VehicleRepository;
import com.roadmateserver.root.service.S3Service;
import com.roadmateserver.root.service.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public VehicleServiceImpl(
            VehicleRepository vehicleRepository,
            ImageRepository imageRepository,
            UserRepository userRepository,
            S3Service S3Service) {
        this.vehicleRepository = vehicleRepository;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.s3Service = S3Service;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VehicleDTO createNewVehicle(final VehicleDTO vehicleDTO, final List<MultipartFile> files) {
        log.info("Creating new vehicle...");
        if (Objects.isNull(vehicleDTO)) {
            log.error("VehicleDTO must not be null");
            throw new IllegalArgumentException("VehicleDTO must not be null");
        }
        log.info("Checking if vehicle with license plate '{}' already exists...", vehicleDTO.getLicensePlate());
        final Optional<VehicleEntity> existingVehicle = vehicleRepository.findByLicensePlate(vehicleDTO.getLicensePlate());
        if (existingVehicle.isPresent()) {
            log.warn("Vehicle with license plate '{}' already exists, returning existing vehicle.", vehicleDTO.getLicensePlate());
            throw new VehicleException("Vehicle with license plate '" + vehicleDTO.getLicensePlate() + "' already exists.");
        }
        log.debug("UserEntity mapping from VehicleDTO owner for vehicle with ID: {}", vehicleDTO.getVehicleId());
        final UserEntity userEntity = userRepository.findByClerkId(vehicleDTO.getOwnerId())
                .orElseThrow(() -> {
                    log.error("User with ID {} not found", vehicleDTO.getOwnerId());
                    return new IllegalArgumentException("User with ID " + vehicleDTO.getOwnerId() + " not found");
                });
        try {
            final List<ImageDTO> images = new ArrayList<>();
            for (MultipartFile imageFile : files) {
                String uploadedUrl = s3Service.uploadFile(imageFile);
                images.add(new ImageDTO(uploadedUrl));
            }
            vehicleDTO.setImages(images);
        } catch (Exception e) {
            log.error("Error uploading images to S3: {}", e.getMessage());
            throw new VehicleException("Error uploading images to S3: " + e.getMessage());
        }
        log.debug("Mapping VehicleDTO to VehicleEntity for vehicle with ID: {}", vehicleDTO.getVehicleId());
        final VehicleEntity vehicleEntity = VehicleDTOEntityMapper.map(vehicleDTO);
        log.debug("Create ImageEntity list from VehicleDTO images for vehicle with ID: {}", vehicleDTO.getVehicleId());
        final List<ImageEntity> imageEntities = vehicleDTO.getImages()
                .stream()
                .map(imageDTO -> {
                    final ImageEntity imageEntity = ImageDTOEntityMapper.map(imageDTO);
                    imageEntity.setVehicle(vehicleEntity);
                    return imageEntity;
                }).toList();
        vehicleEntity.setImages(imageEntities);
        vehicleEntity.setOwner(userEntity);
        log.debug("Saving VehicleEntity to repository for vehicle with ID: {}", vehicleDTO.getVehicleId());
        final VehicleEntity savedVehicleEntity = vehicleRepository.save(vehicleEntity);
        log.debug("Saving ImageEntities to repository for vehicle with ID: {}", savedVehicleEntity.getVehicleId());
        imageRepository.saveAll(imageEntities);
        log.debug("Mapping saved VehicleEntity to VehicleDTO for vehicle with ID: {}", savedVehicleEntity.getVehicleId());
        final VehicleDTO savedVehicleDTO = VehicleDTOEntityMapper.map(savedVehicleEntity);
        log.info("Vehicle successfully created with ID: {}", savedVehicleDTO.getVehicleId());
        return savedVehicleDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VehicleDTO updateVehicle(final VehicleDTO vehicleDTO) {
        if (vehicleDTO == null) {
            log.error("VehicleDTO must not be null");
            throw new IllegalArgumentException("VehicleDTO must not be null");
        }
        log.info("Updating vehicle with ID: {}", vehicleDTO.getVehicleId());
        final VehicleEntity existingVehicle = vehicleRepository.findById(vehicleDTO.getVehicleId())
                .orElseThrow(() -> {
                    log.error("Vehicle not found with ID: {}", vehicleDTO.getVehicleId());
                    return new VehicleNotFoundException("Vehicle not found with ID: " + vehicleDTO.getVehicleId());
                });
        existingVehicle.setBrand(vehicleDTO.getBrand());
        existingVehicle.setModel(vehicleDTO.getModel());
        existingVehicle.setYear(vehicleDTO.getYear());
        existingVehicle.setColor(vehicleDTO.getColor());
        existingVehicle.setLocation(vehicleDTO.getLocation());
        existingVehicle.setCity(vehicleDTO.getCity());
        existingVehicle.setDescription(vehicleDTO.getDescription());
        existingVehicle.setPricePerDay(vehicleDTO.getPricePerDay());
        existingVehicle.setVehicleType(vehicleDTO.getVehicleType());
        existingVehicle.setContactNumber(vehicleDTO.getContactNumber());

        log.info("Updated base fields for vehicle ID: {}", existingVehicle.getVehicleId());
        final List<ImageDTO> imageDTOs = vehicleDTO.getImages();
        log.debug("Clearing old images for vehicle ID: {}", existingVehicle.getVehicleId());
        existingVehicle.getImages().clear();
        imageDTOs.forEach(imageDTO -> {
            log.debug("Mapping ImageDTO to ImageEntity for vehicle ID: {}", existingVehicle.getVehicleId());
            final ImageEntity imageEntity = ImageDTOEntityMapper.map(imageDTO);
            imageEntity.setVehicle(existingVehicle);
            existingVehicle.getImages().add(imageEntity);
        });
        log.debug("Saving updated VehicleEntity to repository for vehicle ID: {}", existingVehicle.getVehicleId());
        final VehicleEntity updatedVehicleEntity = vehicleRepository.save(existingVehicle);
        log.debug("Mapping updated VehicleEntity to VehicleDTO for vehicle ID: {}", updatedVehicleEntity.getVehicleId());
        final VehicleDTO updatedVehicleDTO = VehicleDTOEntityMapper.map(updatedVehicleEntity);
        log.info("Vehicle successfully updated with ID: {}", updatedVehicleDTO.getVehicleId());
        return updatedVehicleDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleDTO getVehicleById(final Integer vehicleId) {
        if (Objects.isNull(vehicleId)) {
            log.error("Failed to fetch vehicle: id is null");
            throw new IllegalArgumentException("id must not be null");
        }
        log.info("Fetching vehicle with id: {}", vehicleId);
        final VehicleEntity vehicleEntity = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> {
                    log.error("Vehicle not found with id: {}", vehicleId);
                    return new IllegalArgumentException("Vehicle not found with id: " + vehicleId);
                });
        log.debug("Creating ImageDTO list from VehicleEntity images for vehicle with ID: {}", vehicleId);
        final List<ImageDTO> imageDTOs = vehicleEntity.getImages().stream()
                .map(ImageDTOEntityMapper::map)
                .toList();
        log.debug(("Mapping VehicleEntity to VehicleDTO for vehicle with id: {}"), vehicleId);
        final VehicleDTO vehicleDTO = VehicleDTOEntityMapper.map(vehicleEntity);
        vehicleDTO.setImages(imageDTOs);
        log.info("Vehicle with id '{}' fetched successfully.", vehicleId);
        return vehicleDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VehicleDTO deleteVehicle(final Integer vehicleId) {
        if (Objects.isNull(vehicleId)) {
            log.error("Failed to delete vehicle: id is null");
            throw new IllegalArgumentException("id must not be null");
        }
        log.info("Deleting vehicle with id: {}", vehicleId);
        final VehicleEntity vehicleEntity = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> {
                    log.error("Vehicle not found with id: {}", vehicleId);
                    return new VehicleNotFoundException("Vehicle not found with id: " + vehicleId);
                });
        vehicleRepository.delete(vehicleEntity);
        log.info("Vehicle with id '{}' deleted successfully.", vehicleId);
        return VehicleDTOEntityMapper.map(vehicleEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> getVehicles() {
        log.info("Fetching all vehicles...");
        final List<VehicleEntity> vehicleEntities = vehicleRepository.findAll();
        final List<VehicleDTO> vehicleDTOs = vehicleEntities.stream()
                .map(vehicleEntity -> {
                    final VehicleDTO vehicleDTO = VehicleDTOEntityMapper.map(vehicleEntity);
                    final List<ImageDTO> imageDTOs = vehicleEntity.getImages().stream()
                            .map(ImageDTOEntityMapper::map)
                            .toList();
                    vehicleDTO.setImages(imageDTOs);
                    return vehicleDTO;
                }).toList();
        log.info("Fetched {} vehicles successfully.", vehicleDTOs.size());
        return vehicleDTOs;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VehicleDTO updateVehicleStatus(final Integer vehicleId, final Constants.VehicleStatus status) {
        if (Objects.isNull(vehicleId) || Objects.isNull(status)) {
            log.error("Vehicle ID and status must not be null");
            throw new IllegalArgumentException("Vehicle ID and status must not be null");
        }
        log.info("Updating vehicle status for vehicle ID: {} to status: {}", vehicleId, status);
        final VehicleEntity vehicleEntity = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> {
                    log.error("Vehicle not found with id: {}", vehicleId);
                    return new VehicleNotFoundException("Vehicle not found with id: " + vehicleId);
                });
        vehicleEntity.setIsAvailable(status);
        log.debug("Saving updated VehicleEntity with ID: {}", vehicleId);
        final VehicleEntity updatedVehicleEntity = vehicleRepository.save(vehicleEntity);
        log.debug("Mapping updated VehicleEntity to VehicleDTO for vehicle ID: {}", updatedVehicleEntity.getVehicleId());
        final VehicleDTO updatedVehicleDTO = VehicleDTOEntityMapper.map(updatedVehicleEntity);
        log.info("Vehicle status updated successfully for vehicle ID: {}", updatedVehicleDTO.getVehicleId());
        return updatedVehicleDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleDTO> getVehiclesByPage(final Integer pageNumber, final Integer pageSize, final String VehicleName) {
        log.info("Fetching all vehicles...");
        final Pageable pageable = PageRequest.of(pageNumber, pageSize);
        final Page<VehicleDTO> vehicleDTOs = vehicleRepository.findAll(VehicleName, pageable)
                .map(vehicleEntity -> {
                    final VehicleDTO vehicleDTO = VehicleDTOEntityMapper.map(vehicleEntity);
                    final List<ImageDTO> imageDTOs = vehicleEntity.getImages().stream()
                            .map(ImageDTOEntityMapper::map)
                            .toList();
                    vehicleDTO.setImages(imageDTOs);
                    return vehicleDTO;
                });
        log.info("Fetched vehicles successfully.");
        return vehicleDTOs;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> getAllVehiclesByOwnerId(final String ownerId) {
        if (Objects.isNull(ownerId)) {
            log.error("Owner ID must not be null");
            throw new IllegalArgumentException("Owner ID must not be null");
        }
        final UserEntity owner = userRepository.findByClerkId(ownerId)
                .orElseThrow(() -> {
                    log.error("Owner not found with ID: {}", ownerId);
                    return new IllegalArgumentException("Owner not found with ID: " + ownerId);
                });
        final List<VehicleEntity> vehicleEntities = vehicleRepository.findByOwner(owner);
        final List<VehicleDTO> vehicleDTOs = vehicleEntities
                .stream()
                .map(vehicleEntity -> {
                    final VehicleDTO vehicleDTO = VehicleDTOEntityMapper.map(vehicleEntity);
                    final List<ImageDTO> imageDTOs = vehicleEntity.getImages()
                            .stream()
                            .map(ImageDTOEntityMapper::map)
                            .toList();
                    vehicleDTO.setImages(imageDTOs);
                    return vehicleDTO;
                })
                .toList();
        log.info("Fetched {} vehicles successfully.", vehicleDTOs.size());
        return vehicleDTOs;
    }
}
