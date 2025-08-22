package com.roadmateserver.root.controller;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.UserDTO;
import com.roadmateserver.root.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
@Validated
@Tag(name = "User Management", description = "Endpoints for managing user accounts and profiles")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create a new user", description = "Register a new user with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(produces = Constants.APPLICATION_JSON, consumes = Constants.APPLICATION_JSON)
    public ResponseEntity<UserDTO> createUser(
            @Parameter(description = "User details to be created", required = true)
            @Valid @RequestBody final UserDTO userDTO) {
        final UserDTO savedUserDTO = userService.createUser(userDTO);
        return ResponseEntity.ok(savedUserDTO);
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a user using their numeric ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "User ID (positive integer)", required = true)
            @Valid @PathVariable @Min(value = 1, message = "user id must be a positive integer") final Integer id) {
        final UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Get user by Clerk ID", description = "Retrieve a user by their unique Clerk identity string.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No user found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping(value = "/clerk/{clerkId}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<UserDTO> getUserByClerkId(
            @Parameter(description = "Clerk ID", required = true)
            @PathVariable final String clerkId) {
        final UserDTO user = userService.getUserByClerkId(clerkId);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Get all users", description = "Retrieve all users, optionally filtered by first name, email, or role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No users found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping(produces = Constants.APPLICATION_JSON)
    public ResponseEntity<Iterable<UserDTO>> getAllUsers(
            @Parameter(description = "Filter by first name")
            @RequestParam(value = "firstName", required = false) List<String> firstNameFilter,
            @Parameter(description = "Filter by email")
            @RequestParam(value = "email", required = false) List<String> emailFilter,
            @Parameter(description = "Filter by role")
            @RequestParam(value = "role", required = false) String roleFilter) {
        final Iterable<UserDTO> users = userService.getAllUsers(firstNameFilter, emailFilter, roleFilter);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Update user details", description = "Update the details of an existing user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(produces = Constants.APPLICATION_JSON, consumes = Constants.APPLICATION_JSON)
    public ResponseEntity<UserDTO> updateUser(
            @Parameter(description = "User details to be updated", required = true)
            @Valid @RequestBody final UserDTO userDTO) {
        final UserDTO updatedUserDTO = userService.updateUser(userDTO);
        return ResponseEntity.ok(updatedUserDTO);
    }

    @Operation(summary = "Delete a user", description = "Delete a user by their unique Clerk ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping(value = "/{clerkId}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<UserDTO> deleteUser(
            @Parameter(description = "Clerk ID of the user to be deleted", required = true)
            @PathVariable final String clerkId) {
        final UserDTO deletedUserDTO = userService.deleteUserByClerkId(clerkId);
        return ResponseEntity.ok(deletedUserDTO);
    }

    @Operation(summary = "Update user role", description = "Assign a new role to a user by their Clerk ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User role updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid role or Clerk ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping(value = "/role/{clerkId}", consumes = Constants.APPLICATION_JSON)
    public ResponseEntity<UserDTO> UpdateUserRole
            (@Parameter(description = "Clerk ID of the user whose role is to be updated", required = true)
            @PathVariable final String clerkId,
            @Parameter(description = "New role for the user", required = true)
            @RequestParam("role") final Constants.UserRole newRole) throws Exception {
        UserDTO updatedUserDTO = userService.updateUserRole(clerkId, newRole);
        if (Objects.nonNull(updatedUserDTO))
            userService.assignStudentRole(clerkId, newRole);
        return ResponseEntity.ok(updatedUserDTO);
    }
}
