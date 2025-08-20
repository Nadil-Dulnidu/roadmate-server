package com.roadmateserver.root.controller;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.PaymentDTO;
import com.roadmateserver.root.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@Validated
@Tag(name = "Payment Management", description = "End points for Payment related operations")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(@Value("${stripe.secret.key}") String secretKey,
                             PaymentService paymentService) {
        Stripe.apiKey = secretKey;
        this.paymentService = paymentService;
    }

    @Operation(summary = "Create a new payment intent",
            description = "Creates a new payment intent with the specified amount in LKR.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment intent created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid amount provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping("/create-payment-intent")
    public Map<String, Object> createPaymentIntent(
            @Parameter(description = "Payment data including amount in LKR",required = true)
            @RequestBody Map<String, Object> data) throws StripeException {
        final Long amount = Long.valueOf(data.get("amount").toString());
        final PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency("LKR")
                .build();
        final PaymentIntent intent = PaymentIntent.create(params);
        Map<String, Object> response = new HashMap<>();
        response.put("clientSecret", intent.getClientSecret());
        return response;
    }

    @Operation(summary = "Create a new payment",
            description = "Creates a new payment record in the system. " +
                    "Requires valid booking ID and user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Payment created successfully",
                    content = @Content(schema = @Schema(implementation = PaymentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid payment data provided"),
            @ApiResponse(responseCode = "404", description = "Booking or User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping(consumes = Constants.APPLICATION_JSON, produces = Constants.APPLICATION_JSON)
    public ResponseEntity<PaymentDTO> cratePayment(
            @Parameter(description = "Payment details to be created", required = true)
            @Valid @RequestBody PaymentDTO paymentDTO) {
        PaymentDTO createdPayment = paymentService.createPayment(paymentDTO);
        return ResponseEntity.ok(createdPayment);
    }

    @Operation(summary = "Get all payments",
            description = "Retrieves all payment records from the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Payments retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PaymentDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping(produces = Constants.APPLICATION_JSON)
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        List<PaymentDTO> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @Operation(summary = "Get payments by user ID",
            description = "Retrieves all payment records associated with a specific user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Payments retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PaymentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user ID provided"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping(value = "/user/{userId}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<List<PaymentDTO>> getPaymentsByUserId(
            @Parameter(description = "ID of the user to fetch payments for", required = true)
            @Valid @PathVariable("userId") String userId) {
        List<PaymentDTO> payments = paymentService.getPaymentsByUserId(userId);
        return ResponseEntity.ok(payments);
    }
}
