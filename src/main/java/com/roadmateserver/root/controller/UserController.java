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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
