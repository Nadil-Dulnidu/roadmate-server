package com.roadmateserver.root.service;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.UserDTO;
import org.springframework.stereotype.Service;
import com.roadmateserver.root.exception.UserException;
import com.roadmateserver.root.exception.UserNotFoundException;

import java.util.List;

@Service
public interface UserService {

    /**
     * Creates a new user in the system.
     * @param userDTO the {@link UserDTO} object containing user details. Must not be {@code null}.
     * @return the created {@link UserDTO} object with any system-generated fields populated (e.g., ID).
     * @throws IllegalArgumentException if the provided userDTO is {@code null} or contains invalid data.
     * @throws UserException if a user with the same email or clerk ID already exists.
     */
    UserDTO createUser(UserDTO userDTO);

    /**
     * Retrieves all users in the system, optionally filtered by first name, email, and role.
     * @param firstNameFilter list of first names to filter users by, can be empty or null for no filtering.
     * @param emailFilter list of emails to filter users by, can be empty or null for no filtering.
     * @param roleFilter the role to filter users by, can be empty or null for no filtering.
     * @return a {@link List} of all {@link UserDTO} objects matching the filters. Never {@code null}, but may be empty.
     * @throws UserException if there is an error retrieving the users
     */
    List<UserDTO> getAllUsers(
            List<Constants.UserRole> userRoles
    );

    /**
     * Deletes a user by their unique identifier.
     * @param userId the ID of the user to delete; must not be {@code null}.
     * @return the {@link UserDTO} object that was deleted.
     * @throws IllegalArgumentException if the provided userId is {@code null} or invalid.
     * @throws UserException if the user with the specified ID does not exist or cannot be deleted.
     *
     */
    UserDTO getUserById(Integer userId);

    /**
     * Retrieves a user by their unique clerk ID.
     * @param clerkId the clerk ID of the user to retrieve; must not be {@code null}.
     * @return the {@link UserDTO} object with the specified clerk ID.
     * @throws IllegalArgumentException if the provided clerkId is {@code null} or invalid.
     * @throws UserNotFoundException if the user with the specified clerk ID does not exist.
     */
    UserDTO getUserByClerkId(String clerkId);

    /**
     * Updates an existing user's information.
     * @param userDTO the {@link UserDTO} object with updated details. Must not be {@code null}.
     * @return the updated {@link UserDTO} object after the changes have been persisted.
     * @throws IllegalArgumentException if the provided userDTO is {@code null} or contains invalid data.
     * @throws UserNotFoundException if the user with the specified ID does not exist.
     */
    UserDTO updateUser(UserDTO userDTO);

    /**
     * Deletes a user by their unique identifier.
     * @param clerkId the ID of the user to delete; must not be {@code null}.
     * @return the {@link UserDTO} object that was deleted.
     * @throws IllegalArgumentException if the provided clerkId is {@code null} or invalid.
     * @throws UserNotFoundException if the user with the specified clerk ID does not exist or cannot be deleted.
     */
    UserDTO deleteUserByClerkId(String clerkId);

    /**
     * Add "student" user role to the clerk public metadata
     * @param userId Unique clerk userId
     * @throws Exception Checked exceptions
     */
    void assignStudentRole(String userId, Constants.UserRole role) throws Exception;

    /**
     * Update the role of a user identified by their clerk ID.
     * @param clerkId the unique identifier for the user whose role is to be updated; must not be {@code null}.
     * @param newRole the new role to assign to the user; must not be {@code null}.
     * @return the updated {@link UserDTO} object with the new role.
     * @throws IllegalArgumentException if either clerkId or newRole is {@code null}.
     * @throws UserNotFoundException if no user with the specified clerk ID exists.
     */
    UserDTO updateUserRole(String clerkId, Constants.UserRole newRole);
}
