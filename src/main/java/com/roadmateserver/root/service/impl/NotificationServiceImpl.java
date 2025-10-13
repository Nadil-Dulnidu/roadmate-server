package com.roadmateserver.root.service.impl;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.AnnouncementRequestDTO;
import com.roadmateserver.root.dto.NotificationDTO;
import com.roadmateserver.root.entity.NotificationEntity;
import com.roadmateserver.root.entity.UserEntity;
import com.roadmateserver.root.exception.NotificationException;
import com.roadmateserver.root.exception.UserNotFoundException;
import com.roadmateserver.root.mapper.NotificationDTOEntityMapper;
import com.roadmateserver.root.repository.NotificationRepository;
import com.roadmateserver.root.repository.UserRepository;
import com.roadmateserver.root.service.NotificationService;
import com.roadmateserver.root.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NotificationDTO sendNotification(final NotificationDTO notificationDTO) {
        if (Objects.isNull(notificationDTO)) {
            log.error("Notification details cannot be null");
            throw new NotificationException("User ID or message cannot be null");
        }
        if (notificationDTO.getMessage().isBlank()) {
            log.error("message cannot be blank");
            throw new NotificationException("message cannot be blank");
        }
        log.info("Sending notification to user with ID: {}", notificationDTO.getUserId());
        final UserEntity userEntity = userRepository.findByClerkId(notificationDTO.getUserId())
                .orElseThrow(() -> {
                    log.error("User with ID {} not found", notificationDTO.getUserId());
                    return new UserNotFoundException("User not found");
                });
        log.info("User found: {}", userEntity);
        final NotificationEntity notificationEntity = NotificationDTOEntityMapper.map(notificationDTO);
        notificationEntity.setUser(userEntity);
        log.info("Saving notification entity: {}", notificationEntity);
        final NotificationEntity savedNotificationEntity = notificationRepository.save(notificationEntity);
        log.info("Notification saved successfully with ID: {}", savedNotificationEntity.getNotificationId());
        return NotificationDTOEntityMapper.map(savedNotificationEntity);
    }

    @Override
    public List<NotificationDTO> getNotificationsByUserId(final String userId) {
        if (Objects.isNull(userId)) {
            log.error("User ID cannot be null");
            throw new NotificationException("User ID cannot be null");
        }
        final List<NotificationDTO> notificationDTOS = notificationRepository.findAllByUser_ClerkId(userId)
                .stream()
                .map(NotificationDTOEntityMapper::map)
                .toList();
        log.info("Notifications retrieved successfully for user ID: {}", userId);
        return notificationDTOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NotificationDTO markNotificationAsRead(final Integer notificationId) {
        if (Objects.isNull(notificationId)) {
            log.error("Notification ID cannot be null");
            throw new NotificationException("Notification ID cannot be null");
        }
        final NotificationEntity notificationEntity = notificationRepository.findById(notificationId)
                .orElseThrow(() -> {
                    log.error("Notification with ID {} not found", notificationId);
                    return new NotificationException("Notification not found");
                });
        notificationEntity.setIsRead(true);
        final NotificationEntity updatedNotificationEntity = notificationRepository.save(notificationEntity);
        log.info("Notification with ID {} marked as read", notificationId);
        return NotificationDTOEntityMapper.map(updatedNotificationEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NotificationDTO deleteNotification(final Integer notificationId) {
        if (Objects.isNull(notificationId)) {
            log.error("Notification ID cannot be null");
            throw new NotificationException("Notification ID cannot be null");
        }
        log.info("Deleting notification with ID: {}", notificationId);
        final NotificationEntity notificationEntity = notificationRepository.findById(notificationId)
                .orElseThrow(() -> {
                    log.error("Notification with ID {} not found", notificationId);
                    return new NotificationException("Notification not found");
                });
        notificationRepository.delete(notificationEntity);
        log.info("Notification deleted successfully with ID: {}", notificationId);
        return NotificationDTOEntityMapper.map(notificationEntity);
    }

    @Async
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createAnnouncementNotification(final AnnouncementRequestDTO announcementRequestDTO) {
        if (Objects.isNull(announcementRequestDTO) || announcementRequestDTO.getMessage().isBlank()) {
            log.error("Announcement details cannot be null or blank");
            throw new NotificationException("Announcement details cannot be null or blank");
        }
        log.info("Creating announcement notification: {}", announcementRequestDTO);
        final List<Constants.UserRole> roles = new ArrayList<>();
        roles.add(Constants.UserRole.RENTER);
        roles.add(Constants.UserRole.OWNER);
        final List<UserEntity> userEntities = userRepository.findAllByRoleIn(roles);
        if (userEntities.isEmpty()) {
            log.warn("No users found for announcement");
            throw new NotificationException("No users found for announcement");
        }
        final List<NotificationEntity> notificationsToSave = userEntities.stream()
                .map(user -> {
                    final NotificationEntity notification = new NotificationEntity();
                    notification.setUser(user);
                    notification.setTitle(announcementRequestDTO.getTitle());
                    notification.setMessage(announcementRequestDTO.getMessage());
                    notification.setNotificationType(announcementRequestDTO.getNotificationType());
                    return notification;
                })
                .collect(Collectors.toList());
        notificationRepository.saveAll(notificationsToSave);
        log.info("Announcement notifications created successfully for {} users", notificationsToSave.size());
    }
}
