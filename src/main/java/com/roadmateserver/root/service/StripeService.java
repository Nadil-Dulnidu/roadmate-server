package com.roadmateserver.root.service;

import com.roadmateserver.root.dto.BookingRequest;
import com.roadmateserver.root.dto.StripeResponse;
import org.springframework.stereotype.Service;

@Service
public interface StripeService {
    /**
     * Creates a Stripe Checkout session for a given user ID and price ID.
     *
     * @param bookingRequest The booking request containing user ID and price ID.
     * @return A URL to redirect the user to complete the payment.
     */
    StripeResponse createCheckoutSession(BookingRequest bookingRequest);
}
