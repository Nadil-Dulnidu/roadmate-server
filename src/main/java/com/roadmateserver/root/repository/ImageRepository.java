package com.roadmateserver.root.repository;

import com.roadmateserver.root.entity.ImageEntity;
import com.roadmateserver.root.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {

    List<ImageEntity> findByVehicle(VehicleEntity vehicle);
}
