package com.roadmateserver.root.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.UserDTO;
import com.roadmateserver.root.entity.UserEntity;
import com.roadmateserver.root.exception.UserException;
import com.roadmateserver.root.exception.UserNotFoundException;
import com.roadmateserver.root.mapper.UserDTOEntityMapper;
import com.roadmateserver.root.repository.UserRepository;
import com.roadmateserver.root.service.UserService;
import com.roadmateserver.root.utils.InterServiceCommunicationHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final InterServiceCommunicationHandler interServiceCommunicationHandler;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           InterServiceCommunicationHandler interServiceCommunicationHandler) {
        this.interServiceCommunicationHandler = interServiceCommunicationHandler;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO createUser(final UserDTO userDTO) {
        log.info("creating new user with clerkId: {}", userDTO.getClerkId());
        if(Objects.isNull(userDTO.getClerkId()) ||
                Objects.isNull(userDTO.getFirstName()) ||
                Objects.isNull(userDTO.getLastName())){
            log.error("Failed to create user: userDTO is null");
            throw new IllegalArgumentException("userDTO is null");
        }
        try{
            final Optional<UserEntity> userEntity = userRepository.findByClerkId(userDTO.getClerkId());
            if(userEntity.isPresent()){
                log.info("User with clerkId '{}' already exists, returning existing user.", userDTO.getClerkId());
                return UserDTOEntityMapper.map(userEntity.get());
            }
            log.debug("mapping UserDTO to UserEntity for clerkId: {}", userDTO.getClerkId());
            final UserEntity newUserEntity = UserDTOEntityMapper.map(userDTO);
            final UserEntity savedUserEntity = userRepository.save(newUserEntity);
            log.info("User '{}' successfully registered.", userDTO.getClerkId());
            return UserDTOEntityMapper.map(savedUserEntity);
        } catch (DataIntegrityViolationException e) {
            log.warn("Data integrity violation while creating user: {}", e.getMessage());
            return userRepository.findByClerkId(userDTO.getClerkId())
                    .map(UserDTOEntityMapper::map)
                    .orElseThrow(() -> {
                        log.error("Failed to create user due to data integrity violation for clerkId: {}",
                                userDTO.getClerkId());
                        return new UserException("Failed to create user due to data integrity violation");
                    });
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers(
            final List<String> firstNameFilter,
            final List<String> emailFilter,
            final String roleFilter) {
        log.info("Fetching all users with filters - firstName: {}, email: {}, role: {}",
                firstNameFilter, emailFilter, roleFilter);
        final List<UserEntity> userEntities = userRepository.findAll();
        final List<UserDTO> result = userEntities
                .stream()
                .filter(user -> firstNameFilter == null || firstNameFilter.isEmpty()
                        || firstNameFilter.contains(user.getFirstName()))
                .filter(user -> emailFilter == null || emailFilter.isEmpty()
                        || emailFilter.contains(user.getEmail()))
                .filter(user -> roleFilter == null || roleFilter.isEmpty()
                        || user.getRole().toString().equalsIgnoreCase(roleFilter))
                .map(user -> {
                    log.debug("Mapping UserEntity to UserDTO for user with id: {}", user.getUserId());
                    return UserDTOEntityMapper.map(user);
                })
                .toList();
        log.info("Total users fetched: {}", result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(final Integer userId) {
        if(Objects.isNull(userId)){
            log.error("Failed to fetch user: id is null");
            throw new IllegalArgumentException("id must not be null");
        }
        log.info("Fetching user with id: {}", userId);
        final UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", userId);
                    return new UserNotFoundException("User not found with id: " + userId);
                });
        log.info("User with id '{}' fetched successfully.", userId);
        return UserDTOEntityMapper.map(userEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserByClerkId(final String clerkId) {
        if(Objects.isNull(clerkId)){
            log.error("Failed to fetch user: id is null");
            throw new IllegalArgumentException("id must not be null");
        }
        log.info("Fetching user with id: {}", clerkId);
        final UserEntity userEntity = userRepository.findByClerkId(clerkId)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", clerkId);
                    return new UserNotFoundException("User not found with id: " + clerkId);
                });
        log.info("User with id '{}' fetched successfully.", clerkId);
        return UserDTOEntityMapper.map(userEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO updateUser(final UserDTO userDTO) {
        log.info("Updating user with clerkId: {}", userDTO.getClerkId());
        if(Objects.isNull(userDTO.getClerkId()) || Objects.isNull(userDTO.getFirstName())
                || Objects.isNull(userDTO.getLastName())){
            log.error("Failed to update user: userDTO is null");
            throw new IllegalArgumentException("userDTO is null");
        }
        final UserEntity existingUser = userRepository.findByClerkId(userDTO.getClerkId())
                .orElseThrow(() -> {
                    log.error("User not found with clerkId: {}", userDTO.getClerkId());
                    return new UserNotFoundException("User not found with clerkId: " + userDTO.getClerkId());
                });
        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setLastName(userDTO.getLastName());
        final UserEntity updatedUserEntity = userRepository.save(existingUser);
        log.info("User with clerkId '{}' successfully updated.", userDTO.getClerkId());
        return UserDTOEntityMapper.map(updatedUserEntity);
    }

    @Override
    public UserDTO deleteUserByClerkId(final String clerkId) {
        if(Objects.isNull(clerkId)){
            log.error("Failed to delete user: clerkId is null");
            throw new IllegalArgumentException("clerkId must not be null");
        }
        log.info("Deleting user with clerkId: {}", clerkId);
        final UserEntity userEntity = userRepository.findByClerkId(clerkId)
                .orElseThrow(() -> {
                    log.error("User not found with clerkId: {}", clerkId);
                    return new UserNotFoundException("User not found with clerkId: " + clerkId);
                });
        userRepository.delete(userEntity);
        log.info("User with clerkId '{}' successfully deleted.", clerkId);
        return UserDTOEntityMapper.map(userEntity);
    }

    @Override
    public void assignStudentRole(final String userId, final Constants.UserRole role) throws Exception {
        log.info("Assigning student role to user with ID: {}", userId);
        final String userResponse = interServiceCommunicationHandler.getUserId(userId);
        log.debug("Fetched user info from clerk");
        final Map<String, Object> userMap = objectMapper.readValue(userResponse, Map.class);
        Map<String, Object> publicMetadata = (Map<String, Object>) userMap.get("public_metadata");
        if (publicMetadata == null) {
            publicMetadata = new HashMap<>();
        }
        publicMetadata.put("role", role);
        final Map<String, Object> updatePayload = new HashMap<>();
        updatePayload.put("public_metadata", publicMetadata);
        log.debug("Updating student role");
        interServiceCommunicationHandler.updateUserMetaData(updatePayload,userId);
        log.info("Student role assigned successfully for user with ID: {}", userId);
    }


}
