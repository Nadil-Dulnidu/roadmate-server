package com.roadmateserver.root.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "image")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", unique = true)
    private Integer imageId;

    @Column(name = "image_url", nullable = false)
    @NotBlank(message = "Image URL must not be blank")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    @NotNull(message = "Vehicle ID must not be null")
    private VehicleEntity vehicle;
}
