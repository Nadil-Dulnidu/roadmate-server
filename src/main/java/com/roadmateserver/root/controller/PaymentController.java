package com.roadmateserver.root.controller;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.dto.BookingRequest;
import com.roadmateserver.root.dto.StripeResponse;
import com.roadmateserver.root.service.StripeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final StripeService stripeService;

    @Autowired
    public PaymentController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping(value = "/checkout", consumes = Constants.APPLICATION_JSON, produces = Constants.APPLICATION_JSON)
    public ResponseEntity<StripeResponse> createCheckoutSession(
            @Valid
            @RequestBody BookingRequest bookingRequest) {
        StripeResponse stripeResponse = stripeService.createCheckoutSession(bookingRequest);
        return ResponseEntity.ok(stripeResponse);
    }
}
