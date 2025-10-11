package com.roadmateserver.root.controller;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.AnnouncementRequestDTO;
import com.roadmateserver.root.dto.NotificationDTO;
import com.roadmateserver.root.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@Validated
@Tag(name = "Notification Service", description = "Endpoints for managing user notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(summary = "Get notifications by user ID",
            description = "Fetches all notifications associated with a specific user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user ID provided"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @GetMapping(value = "/user/{userId}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<List<NotificationDTO>> getNotificationsByUserId(
            @Parameter(description = "ID of the user to fetch notifications for", required = true)
            @PathVariable final String userId) {
        final List<NotificationDTO> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping(consumes = Constants.APPLICATION_JSON, produces = Constants.APPLICATION_JSON)
    @Operation(summary = "Send a notification",
            description = "Sends a notification to a user with the specified message.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid notification data provided"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    public ResponseEntity<NotificationDTO> sendNotification(
            @Parameter(description = "Notification details to be sent", required = true)
            @Valid
            @RequestBody final NotificationDTO notificationDTO) {
        final NotificationDTO sentNotification = notificationService.sendNotification(notificationDTO);
        return ResponseEntity.ok(sentNotification);
    }

    @Operation(summary = "Delete a notification",
            description = "Deletes a notification by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid notification ID provided"),
            @ApiResponse(responseCode = "404", description = "Notification not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @DeleteMapping(value = "/{notificationId}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<NotificationDTO> deleteNotification(
            @Parameter(description = "ID of the notification to be deleted", required = true)
            @Valid
            @Min(value = 1, message = "Notification ID must be greater than 0")
            @PathVariable("notificationId") final Integer notificationId) {
        final NotificationDTO deletedNotification = notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok(deletedNotification);
    }

    @Operation(summary = "Mark notification as read",
            description = "Marks a notification as read by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification marked as read successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid notification ID provided"),
            @ApiResponse(responseCode = "404", description = "Notification not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PatchMapping(value = "/{notificationId}/read", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<NotificationDTO> markNotificationAsRead(
            @Parameter(description = "ID of the notification to be marked as read", required = true)
            @Valid
            @Min(value = 1, message = "Notification ID must be greater than 0")
            @PathVariable("notificationId") final Integer notificationId) {
        final NotificationDTO updatedNotification = notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok(updatedNotification);
    }

    @PostMapping(value = "/announcement", consumes = Constants.APPLICATION_JSON)
    @Operation(summary = "Create an announcement notification",
            description = "Creates an announcement notification to be sent to all users.")    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Announcement notification created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid announcement data provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    public ResponseEntity<Void> createAnnouncementNotification(
            @Parameter(description = "Announcement details to be created", required = true)
            @Valid @RequestBody final AnnouncementRequestDTO announcementRequestDTO) {
        notificationService.createAnnouncementNotification(announcementRequestDTO);
        return ResponseEntity.ok().build();
    }
}
