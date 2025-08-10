package com.roadmateserver.root.repository;

import com.roadmateserver.root.entity.UserEntity;
import com.roadmateserver.root.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Integer> {

    Optional<VehicleEntity> findByLicensePlate(String licensePlate);

    List<VehicleEntity> findByOwner(UserEntity owner);
}
