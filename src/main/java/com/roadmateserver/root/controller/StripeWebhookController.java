package com.roadmateserver.root.controller;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe")
@Tag(name = "Stripe Webhook", description = "Endpoints for handling Stripe webhooks")
public class StripeWebhookController {

    @Value("${stripe.webhook.secret}")
    private String WEBHOOK_SECRET;

    @PostMapping("/webhook")
    public String handleStripeEvent(@RequestBody String payload,
                                    @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, WEBHOOK_SECRET);
        } catch (Exception e) {
            return "Invalid signature";
        }

        switch (event.getType()) {
            case "checkout.session.completed":
                Session session = (Session) event.getDataObjectDeserializer()
                        .getObject()
                        .orElse(null);
                if (session != null) {
                    System.out.println("Payment successful for session: " + session.getId() + session.getPaymentIntent() +
                            " with amount: " + session.getAmountTotal() + session.getCustomerEmail() + session.getPaymentStatus() + session.getCustomerDetails().getName());
                }
                break;

            case "payment_intent.payment_failed":
                PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                        .getObject()
                        .orElse(null);
                if (paymentIntent != null) {
                    System.out.println("Payment failed for: " + paymentIntent.getId());
                }
                break;
        }
        return "";
    }
}

