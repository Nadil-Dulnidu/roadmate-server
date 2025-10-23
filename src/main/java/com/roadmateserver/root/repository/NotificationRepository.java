package com.roadmateserver.root.repository;

import com.roadmateserver.root.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {

    /**
     * Finds all notifications for a specific user by their clerk ID.
     *
     * @param userClerkId the clerk ID of the user
     * @return a list of NotificationEntity objects associated with the specified user
     */
    List<NotificationEntity> findAllByUser_ClerkId(String userClerkId);
}
