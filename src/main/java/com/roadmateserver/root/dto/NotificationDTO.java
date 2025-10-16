package com.roadmateserver.root.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.roadmateserver.root.common.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {

    @JsonProperty("notification_id")
    private Integer notificationId;

    @JsonProperty("user_id")
    @NotBlank(message = "User ID must not be blank")
    private String userId;

    @JsonProperty("title")
    @NotBlank(message = "Title must not be blank")
    private String title;

    @JsonProperty("notification_type")
    @NotNull(message = "Notification type must not be null")
    private Constants.NotificationType notificationType;

    @JsonProperty("message")
    @NotBlank(message = "Message must not be blank")
    private String message;

    @JsonProperty("is_read")
    private Boolean isRead = false;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
