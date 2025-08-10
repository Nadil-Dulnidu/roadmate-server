package com.roadmateserver.root.mapper;

import com.roadmateserver.root.dto.ImageDTO;
import com.roadmateserver.root.entity.ImageEntity;
import java.util.Objects;

public class ImageDTOEntityMapper {
    public static ImageDTO map(ImageEntity imageEntity) {
        if (Objects.isNull(imageEntity))
            throw new IllegalArgumentException("ImageEntity must not be null");
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setImageId(imageEntity.getImageId());
        if(Objects.isNull(imageEntity.getVehicle()))
            throw new IllegalArgumentException("Vehicle must not be null in ImageEntity");
        imageDTO.setVehicleId(imageEntity.getVehicle().getVehicleId());
        imageDTO.setImageUrl(imageEntity.getImageUrl());
        return imageDTO;
    }

    public static ImageEntity map(ImageDTO imageDTO) {
        if (Objects.isNull(imageDTO))
            throw new IllegalArgumentException("ImageDTO must not be null");
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setImageId(imageDTO.getImageId());
        imageEntity.setImageUrl(imageDTO.getImageUrl());
        return imageEntity;
    }
}
