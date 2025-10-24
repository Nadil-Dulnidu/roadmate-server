package com.roadmateserver.root.controller;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.AnnouncementRequestDTO;
import com.roadmateserver.root.dto.NotificationDTO;
import com.roadmateserver.root.dto.cache.NotificationListCache;
import com.roadmateserver.root.service.NotificationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@Validated
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping(value = "/user/{userId}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<List<NotificationDTO>> getNotificationsByUserId(
            @PathVariable final String userId) {
        final NotificationListCache notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications.getNotificationList());
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
            @Min(value = 1, message = "Notification ID must be greater than 0")
            @PathVariable("notificationId") final Integer notificationId) {
        final NotificationDTO deletedNotification = notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok(deletedNotification);
    }

    @PatchMapping(value = "/{notificationId}/read", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<NotificationDTO> markNotificationAsRead(
            @Min(value = 1, message = "Notification ID must be greater than 0")
            @PathVariable("notificationId") final Integer notificationId) {
        final NotificationDTO updatedNotification = notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok(updatedNotification);
    }

    @PreAuthorize(Constants.ADMIN_OR_STAFF_ROLE_PERMISSION)
    @PostMapping(value = "/announcement", consumes = Constants.APPLICATION_JSON)
    public ResponseEntity<Void> createAnnouncementNotification(
            @Valid @RequestBody final AnnouncementRequestDTO announcementRequestDTO) {
        notificationService.createAnnouncementNotification(announcementRequestDTO);
        return ResponseEntity.ok().build();
    }
}
