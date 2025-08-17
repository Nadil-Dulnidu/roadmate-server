package com.roadmateserver.root.service.impl;

import com.roadmateserver.root.dto.BookingRequest;
import com.roadmateserver.root.dto.StripeResponse;
import com.roadmateserver.root.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class StripeServiceImpl implements StripeService {

    @Value("${stripe.secret.key}")
    private String secretKey;

    @Override
    public StripeResponse createCheckoutSession(BookingRequest bookingRequest) {
        Stripe.apiKey = secretKey;
        SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(bookingRequest.getName()).build();
        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency(Objects.isNull(bookingRequest.getCurrency()) ? "USD" : bookingRequest.getCurrency())
                .setUnitAmount(bookingRequest.getAmount())
                .setProductData(productData)
                .build();
        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(bookingRequest.getQuantity())
                .setPriceData(priceData)
                .build();
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/success")
                .setCancelUrl("http://localhost:8080/cancel")
                .setCustomerCreation(SessionCreateParams.CustomerCreation.IF_REQUIRED)
                .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                .addLineItem(lineItem)
                .build();
        Session session;
        try {
            session = Session.create(params);
        }catch (StripeException e) {
            throw new RuntimeException("Failed to create Stripe session: " + e.getMessage(), e);
        }
        return StripeResponse.builder()
                .sessionId(session.getId())
                .sessionUrl(session.getUrl())
                .message("Checkout session created successfully")
                .status(session.getStatus())
                .build();
    }
}
