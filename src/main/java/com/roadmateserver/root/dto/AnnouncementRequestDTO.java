package com.roadmateserver.root.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.roadmateserver.root.common.Constants;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementRequestDTO {

    @JsonProperty("title")
    @NotBlank(message = "Announcement title cannot be blank")
    private String title;

    @JsonProperty("notification_type")
    @NotBlank(message = "Announcement type cannot be blank")
    private Constants.NotificationType notificationType;

    @JsonProperty("message")
    @NotBlank(message = "Announcement message cannot be blank")
    private String message;
}
