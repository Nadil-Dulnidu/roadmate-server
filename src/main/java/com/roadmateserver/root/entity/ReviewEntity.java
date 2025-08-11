package com.roadmateserver.root.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = {"user", "vehicle"})
@Table(name = "review")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", unique = true)
    private Integer reviewId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private VehicleEntity vehicle;

    @Column(name = "review_text", nullable = false, length = 1500)
    @NotBlank(message = "Review text must not be null")
    private String reviewText;

    @Column(name = "reviewer_name", nullable = false)
    @NotBlank(message = "Reviewer name must not be null")
    private String reviewerName;

    @Column(name = "rating", nullable = false)
    @NotNull(message = "Rating must not be null")
    private Double rating;

    @Column(name = "created_at", nullable = false)
    @NotNull(message = "Created at must not be null")
    private LocalDateTime createdAt;
}
