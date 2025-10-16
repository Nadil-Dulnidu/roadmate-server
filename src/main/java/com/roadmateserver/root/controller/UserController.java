package com.roadmateserver.root.controller;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.UserDTO;
import com.roadmateserver.root.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(produces = Constants.APPLICATION_JSON, consumes = Constants.APPLICATION_JSON)
    public ResponseEntity<UserDTO> createUser(
            @Valid @RequestBody final UserDTO userDTO) {
        final UserDTO savedUserDTO = userService.createUser(userDTO);
        return ResponseEntity.ok(savedUserDTO);
    }

    @GetMapping(value = "/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<UserDTO> getUserById(
            @PathVariable @Min(value = 1, message = "user id must be a positive integer") final Integer id) {
        final UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "/clerk/{clerkId}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<UserDTO> getUserByClerkId(
            @PathVariable final String clerkId) {
        final UserDTO user = userService.getUserByClerkId(clerkId);
        return ResponseEntity.ok(user);
    }

    @GetMapping(produces = Constants.APPLICATION_JSON)
    public ResponseEntity<List<UserDTO>> getAllUsers(
            @RequestParam(value = "role", required = false) List<Constants.UserRole> userRoles) {
        final List<UserDTO> users = userService.getAllUsers(userRoles);
        return ResponseEntity.ok(users);
    }

    @PutMapping(produces = Constants.APPLICATION_JSON, consumes = Constants.APPLICATION_JSON)
    public ResponseEntity<UserDTO> updateUser(
            @Valid @RequestBody final UserDTO userDTO) {
        final UserDTO updatedUserDTO = userService.updateUser(userDTO);
        return ResponseEntity.ok(updatedUserDTO);
    }


    @DeleteMapping(value = "/{clerkId}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<UserDTO> deleteUser(
            @PathVariable final String clerkId) {
        final UserDTO deletedUserDTO = userService.deleteUserByClerkId(clerkId);
        return ResponseEntity.ok(deletedUserDTO);
    }

    @PatchMapping(value = "/role/{clerkId}", consumes = Constants.APPLICATION_JSON)
    public ResponseEntity<UserDTO> UpdateUserRole(
            @PathVariable final String clerkId,
            @RequestParam("role") final Constants.UserRole newRole) throws Exception {
        UserDTO updatedUserDTO = userService.updateUserRole(clerkId, newRole);
        if (Objects.nonNull(updatedUserDTO))
            userService.assignStudentRole(clerkId, newRole);
        return ResponseEntity.ok(updatedUserDTO);
    }
}
