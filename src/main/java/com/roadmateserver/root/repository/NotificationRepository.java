package com.roadmateserver.root.repository;

import com.roadmateserver.root.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {

    /**
     * Finds all notifications for a specific user by their clerk ID.
     *
     * @param userClerkId the clerk ID of the user
     * @return a list of NotificationEntity objects associated with the specified user
     */
    List<NotificationEntity> findAllByUser_ClerkId(String userClerkId);
}
