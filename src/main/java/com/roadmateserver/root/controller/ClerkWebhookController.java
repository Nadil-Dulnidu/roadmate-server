package com.roadmateserver.root.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.UserDTO;
import com.roadmateserver.root.exception.ClerkException;
import com.roadmateserver.root.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/clerk")
public class ClerkWebhookController {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserService userService;

    ClerkWebhookController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<?> handleClerkUser(
            @RequestBody final String rawBody) throws ClerkException {
        try {
            final Map<String, Object> body = objectMapper.readValue(rawBody, Map.class);
            final Map<String, Object> data = (Map<String, Object>) body.get("data");
            final JsonNode root = objectMapper.readTree(rawBody);
            if (data == null || data.get("id") == null) {
                throw new IllegalArgumentException("Invalid data: Missing userId");
            }
            final String userId = data.get("id").toString();

            if(body.get("type").equals("user.deleted")){
                userService.deleteUserByClerkId(userId);
                return new ResponseEntity<>(HttpStatus.OK);
            }

            final JsonNode emailNode = root.path("data")
                    .path("email_addresses")
                    .get(0).path("email_address");
            final Long createdAt = (Long) data.get("created_at");
            final Instant createdAtDateTime = Instant.ofEpochMilli(createdAt);
            if(body.get("type").equals("user.updated")){
                final UserDTO user = new UserDTO(
                        userId,
                        data.get("first_name").toString(),
                        data.get("last_name").toString(),
                        emailNode.asText(),
                        Constants.UserRole.RENTER,
                        createdAtDateTime
                );
                final UserDTO updatedUser = userService.updateUser(user);
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            }
            final UserDTO userDTO = new UserDTO(
                    userId,
                    Objects.nonNull(data.get("first_name").toString()) ? data.get("first_name").toString() : "-",
                    Objects.nonNull(data.get("last_name").toString()) ? data.get("last_name").toString() : "-",
                    emailNode.asText(),
                    Constants.UserRole.RENTER,
                    createdAtDateTime
            );
            final UserDTO savedUser = userService.createUser(userDTO);
            userService.assignStudentRole(userId, Constants.UserRole.RENTER);
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        } catch (Exception e) {
            throw new ClerkException(e.getMessage());
        }
    }
}
