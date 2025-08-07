package com.roadmateserver.root.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notification")
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id", unique = true)
    private Integer notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "message", nullable = false, length = 1500)
    @NotBlank(message = "Message must not be blank")
    private String message;

    @Column(name = "is_read", nullable = false)
    @NotNull(message = "Read status must not be null")
    private Boolean isRead = false;

    @Column(name = "created_at", nullable = false)
    @NotNull(message = "Created at must not be null")
    private LocalDateTime createdAt;

}
