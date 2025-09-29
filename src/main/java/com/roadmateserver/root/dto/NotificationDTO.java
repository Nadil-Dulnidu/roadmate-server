package com.roadmateserver.root.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.roadmateserver.root.common.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data Transfer Object representing a notification in the system")
public class NotificationDTO {

    @Schema(description = "Unique identifier for the notification", example = "1")
    @JsonProperty("notification_id")
    private Integer notificationId;

    @Schema(description = "Unique identifier for the user associated with the notification", example = "12345")
    @JsonProperty("user_id")
    @NotBlank(message = "User ID must not be blank")
    private String userId;

    @Schema(description = "Title of the notification", example = "Ride Confirmation")
    @JsonProperty("title")
    @NotBlank(message = "Title must not be blank")
    private String title;

    @Schema(description = "Type of the notification", example = "BOOKING")
    @JsonProperty("notification_type")
    @NotNull(message = "Notification type must not be null")
    private Constants.NotificationType notificationType;

    @Schema(description = "Message content of the notification", example = "Your ride has been confirmed.")
    @JsonProperty("message")
    @NotBlank(message = "Message must not be blank")
    private String message;

    @Schema(description = "Indicates whether the notification has been read", example = "false")
    @JsonProperty("is_read")
    private Boolean isRead = false;

    @Schema(description = "Timestamp when the notification was created", example = "2023-10-01T12:00:00")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
