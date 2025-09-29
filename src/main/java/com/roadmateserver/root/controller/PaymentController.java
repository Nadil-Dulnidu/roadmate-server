package com.roadmateserver.root.controller;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.PaymentDTO;
import com.roadmateserver.root.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
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
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(@Value("${stripe.secret.key}") String secretKey,
                             PaymentService paymentService) {
        Stripe.apiKey = secretKey;
        this.paymentService = paymentService;
    }

    @PostMapping("/create-payment-intent")
    public Map<String, Object> createPaymentIntent(
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

    @PostMapping(consumes = Constants.APPLICATION_JSON, produces = Constants.APPLICATION_JSON)
    public ResponseEntity<PaymentDTO> cratePayment(
            @Valid @RequestBody PaymentDTO paymentDTO) {
        PaymentDTO createdPayment = paymentService.createPayment(paymentDTO);
        return ResponseEntity.ok(createdPayment);
    }

    @GetMapping(produces = Constants.APPLICATION_JSON)
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        List<PaymentDTO> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping(value = "/user/{userId}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<List<PaymentDTO>> getPaymentsByUserId(
            @Valid @PathVariable("userId") String userId) {
        List<PaymentDTO> payments = paymentService.getPaymentsByUserId(userId);
        return ResponseEntity.ok(payments);
    }
}
