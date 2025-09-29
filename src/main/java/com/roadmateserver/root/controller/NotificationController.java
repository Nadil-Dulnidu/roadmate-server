package com.roadmateserver.root.controller;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.NotificationDTO;
import com.roadmateserver.root.service.NotificationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@Validated
public class NotificationController {
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping(value = "/user/{userId}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<List<NotificationDTO>> getNotificationsByUserId(
            @Valid
            @Min(value = 1, message = "User ID must be greater than 0")
            @PathVariable("userId") final String userId) {
        final List<NotificationDTO> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping(consumes = Constants.APPLICATION_JSON, produces = Constants.APPLICATION_JSON)
    public ResponseEntity<NotificationDTO> sendNotification(
            @Valid
            @RequestBody final NotificationDTO notificationDTO) {
        final NotificationDTO sentNotification = notificationService.sendNotification(notificationDTO);
        return ResponseEntity.ok(sentNotification);
    }

    @DeleteMapping(value = "/{notificationId}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<NotificationDTO> deleteNotification(
            @Valid
            @Min(value = 1, message = "Notification ID must be greater than 0")
            @PathVariable("notificationId") final Integer notificationId) {
        final NotificationDTO deletedNotification = notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok(deletedNotification);
    }

    @PatchMapping(value = "/{notificationId}/read", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<NotificationDTO> markNotificationAsRead(
            @Valid
            @Min(value = 1, message = "Notification ID must be greater than 0")
            @PathVariable("notificationId") final Integer notificationId) {
        final NotificationDTO updatedNotification = notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok(updatedNotification);
    }
}
