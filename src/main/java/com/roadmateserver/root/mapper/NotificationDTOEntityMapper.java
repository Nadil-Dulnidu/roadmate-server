package com.roadmateserver.root.mapper;

import com.roadmateserver.root.dto.NotificationDTO;
import com.roadmateserver.root.entity.NotificationEntity;

import java.util.Objects;

public class NotificationDTOEntityMapper {

    public static NotificationDTO map(NotificationEntity notificationEntity) {
        if (Objects.isNull(notificationEntity))
            throw new IllegalArgumentException("Notification entity is null");
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setNotificationId(notificationEntity.getNotificationId());
        if(Objects.isNull(notificationEntity.getUser()))
            throw new IllegalArgumentException("User in notification entity is null");
        notificationDTO.setUserId(notificationEntity.getUser().getClerkId());
        notificationDTO.setMessage(notificationEntity.getMessage());
        notificationDTO.setCreatedAt(notificationEntity.getCreatedAt());
        notificationDTO.setIsRead(notificationEntity.getIsRead());
        return notificationDTO;
    }

    public static NotificationEntity map(NotificationDTO notificationDTO) {
        if (Objects.isNull(notificationDTO))
            throw new IllegalArgumentException("Notification DTO is null");
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setNotificationId(notificationDTO.getNotificationId());
        notificationEntity.setMessage(notificationDTO.getMessage());
        notificationEntity.setCreatedAt(notificationDTO.getCreatedAt());
        notificationEntity.setIsRead(notificationDTO.getIsRead());
        return notificationEntity;
    }
}
