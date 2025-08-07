package com.roadmateserver.root.mapper;



import com.roadmateserver.root.dto.UserDTO;
import com.roadmateserver.root.entity.UserEntity;

import java.util.Objects;

public class UserDTOEntityMapper {

    public static UserEntity map(UserDTO userDTO) {
        if(Objects.isNull(userDTO)) throw new IllegalArgumentException("userDTO is null");
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setClerkId(userDTO.getClerkId());
        userEntity.setRole(userDTO.getRole());
        userEntity.setCreatedAt(userDTO.getCreatedAt());
        return userEntity;
    }

    public static UserDTO map(UserEntity userEntity) {
        if(Objects.isNull(userEntity)) throw new IllegalArgumentException("userEntity is null");
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userEntity.getUserId());
        userDTO.setFirstName(userEntity.getFirstName());
        userDTO.setLastName(userEntity.getLastName());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setClerkId(userEntity.getClerkId());
        userDTO.setRole(userEntity.getRole());
        userDTO.setCreatedAt(userEntity.getCreatedAt());
        return userDTO;
    }
}
