package com.roadmateserver.root.entity;

import com.roadmateserver.root.common.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicle_listing")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id", unique = true)
    private Integer vehicleId;

    @Column(name = "vehicle_type", nullable = false)
    @NotNull(message = "Vehicle type must not be null")
    @Enumerated(EnumType.STRING)
    private Constants.VehicleType vehicleType;

    @Column(name = "brand", nullable = false)
    @NotBlank(message = "Brand must not be null")
    private String brand;

    @Column(name = "model", nullable = false)
    @NotBlank(message = "Model must not be null")
    private String model;

    @Column(name = "year", nullable = false)
    @NotNull(message = "Year must not be null")
    private Integer year;

    @Column(name = "color", nullable = false)
    @NotBlank(message = "Color must not be null")
    private String color;

    @Column(name = "license_plate", nullable = false, unique = true)
    @NotBlank(message = "License plate must not be null")
    private String licensePlate;

    @Column(name = "description", nullable = false, length = 1500)
    @NotBlank(message = "Description must not be blank")
    private String description;

    @Column(name = "price_per_day", nullable = false)
    @NotNull(message = "Price per day must not be null")
    private Double pricePerDay;

    @Column(name = "location", nullable = false)
    @NotBlank(message = "Location must not be blank")
    private String location;

    @Column(name = "city", nullable = false)
    @NotBlank(message = "City must not be blank")
    private String city;

    @Column(name = "contact_number", nullable = false)
    @NotBlank(message = "Contact number must not be blank")
    private String contactNumber;

    @Column(name = "availability", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Availability must not be null")
    private Constants.VehicleStatus isAvailable = Constants.VehicleStatus.AVAILABLE;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    @NotNull(message = "Owner must not be null")
    private UserEntity owner;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ImageEntity> images = new ArrayList<>();

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ReviewEntity> reviews;
}
