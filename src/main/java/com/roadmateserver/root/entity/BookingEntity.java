package com.roadmateserver.root.entity;

import com.roadmateserver.root.common.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"renter", "vehicle"})
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id", unique = true)
    private Integer bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renter_id", nullable = false)
    private UserEntity renter;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "vehicle_id", nullable = false)
    private VehicleEntity vehicle;

    @Column(name = "start_date", nullable = false)
    @NotBlank(message = "Start date must not be blank")
    private String startDate;

    @Column(name = "end_date", nullable = false)
    @NotBlank(message = "End date must not be blank")
    private String endDate;

    @Column(name = "total_price", nullable = false)
    @NotNull(message = "Total price must not be blank")
    private Double totalPrice;

    @Column(name = "created_at", nullable = false)
    @NotNull(message = "Created at must not be blank")
    private LocalDateTime createdAt;

    @Column(name = "status", nullable = false)
    @NotNull(message = "Status must not be null")
    @Enumerated(EnumType.STRING)
    private Constants.BookingStatus status = Constants.BookingStatus.PENDING;
}
