package com.roadmateserver.root.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "payment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"booking", "user"})
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", unique = true)
    private Integer paymentId;

    @Column(name = "stripe_id", nullable = false, unique = true)
    @NotBlank(message = "Stripe ID must not be blank")
    private String stripeId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_id", referencedColumnName = "booking_id", nullable = false)
    @NotNull(message = "Booking must not be null")
    private BookingEntity booking;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    @NotNull(message = "User must not be null")
    private UserEntity user;

    @Column(name = "amount", nullable = false)
    @NotNull(message = "Amount must not be null")
    private Double amount;

    @Column(name = "status", nullable = false)
    @NotBlank(message = "Payment status must not be blank")
    private String status;

    @Column(name = "date", nullable = false)
    @NotNull(message = "Payment date must not be null")
    private String date;

}
