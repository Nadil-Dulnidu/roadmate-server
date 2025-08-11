package com.roadmateserver.root.entity;

import com.roadmateserver.root.common.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"vehicles", "notifications", "bookings", "reviews", "payments"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true)
    private Integer userId;

    @Column(name = "clerk_id", unique = true, nullable = false)
    @NotBlank(message = "Clerk ID must not be blank")
    private String clerkId;

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "First name must not be blank")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "Last name must not be blank")
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    @NotBlank(message = "Email must not be blank")
    @Email
    private String email;

    @Column(name = "created_at", nullable = false)
    @NotNull(message = "Created at must not be null")
    private LocalDateTime createdAt;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role must not be null")
    private Constants.UserRole role = Constants.UserRole.RENTER;

    @OneToMany(mappedBy = "owner")
    private List<VehicleEntity> vehicles;

    @OneToMany(mappedBy = "user")
    private List<NotificationEntity> notifications;

    @OneToMany(mappedBy = "renter")
    private List<BookingEntity> bookings;

    @OneToMany(mappedBy = "user")
    private List<ReviewEntity> reviews;

    @OneToMany(mappedBy = "user")
    private List<PaymentEntity> payments;
}
