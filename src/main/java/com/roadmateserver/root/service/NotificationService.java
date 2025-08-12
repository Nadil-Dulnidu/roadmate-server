package com.roadmateserver.root.service;

import com.roadmateserver.root.dto.NotificationDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {

    /**
     * Sends a notification to the user.
     *
     * @param notificationDTO the details of the notification to be sent
     *
     * @return a NotificationDTO containing the details of the send notification
     */
    NotificationDTO sendNotification(NotificationDTO notificationDTO);

    /**
     * Retrieves notifications for a specific user.
     *
     * @param userId the ID of the user whose notifications are to be retrieved
     * @return a list of NotificationDTOs containing notifications for the specified user
     */
    List<NotificationDTO> getNotificationsByUserId(String userId);

    /**
     * Marks a notification as read.
     *
     * @param notificationId the ID of the notification to be marked as read
     * @return a NotificationDTO containing the updated details of the notification
     */
    NotificationDTO markNotificationAsRead(Integer notificationId);

    /**
     * Deletes a notification.
     *
     * @param notificationId the ID of the notification to be deleted
     * @return a NotificationDTO containing the details of the deleted notification
     */
    NotificationDTO deleteNotification(Integer notificationId);





}
