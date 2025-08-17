package com.roadmateserver.root.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.UserDTO;
import com.roadmateserver.root.exception.ClerkException;
import com.roadmateserver.root.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/clerk")
@Tag(name = "Clerk Webhook", description = "Endpoints for handling Clerk webhooks")
public class ClerkWebhookController {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserService userService;

    ClerkWebhookController(UserService userService) {
        this.userService = userService;
    }
    @Operation(
            summary = "Handle Clerk user webhook",
            description = "Handles `user.created` and `user.updated` events from Clerk and maps to internal StudentDTO logic.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User processed successfully (created or updated)"),
                    @ApiResponse(responseCode = "400", description = "Bad request or missing fields"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PostMapping("/user")
    public ResponseEntity<?> handleClerkUser(
            @Parameter(description = "Raw JSON body from Clerk webhook")
            @RequestBody final String rawBody) throws ClerkException {
        try {
            final Map<String, Object> body = objectMapper.readValue(rawBody, Map.class);
            final Map<String, Object> data = (Map<String, Object>) body.get("data");
            final JsonNode root = objectMapper.readTree(rawBody);
            if (data == null || data.get("id") == null) {
                throw new IllegalArgumentException("Invalid data: Missing userId");
            }
            final String userId = data.get("id").toString();
            final JsonNode emailNode = root.path("data")
                    .path("email_addresses")
                    .get(0).path("email_address");
            final Long createdAt = (Long) data.get("created_at");
            final LocalDateTime createdAtDateTime = Instant.ofEpochSecond(createdAt)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

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
            userService.assignStudentRole(userId);
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        } catch (Exception e) {
            throw new ClerkException(e.getMessage());
        }
    }
}
