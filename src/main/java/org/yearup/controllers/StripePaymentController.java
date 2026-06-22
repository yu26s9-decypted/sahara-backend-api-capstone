package org.yearup.controllers;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.SubscriptionCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.User;
import org.yearup.service.UserService;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@CrossOrigin
public class StripePaymentController {
    private final UserService userService;

    public StripePaymentController(UserService userService) {
        this.userService = userService;
    }

    private int getUserId(Principal principal){
        String userName = principal.getName();
        User user = userService.getByUserName(userName);

        return user.getId();
    }

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody Map<String, Object> body) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        long amount = Long.parseLong(body.get("amount").toString());

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency("usd")
                .build();

        PaymentIntent intent = PaymentIntent.create(params);

        return ResponseEntity.ok(Map.of("clientSecret", intent.getClientSecret()));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create-subscription")
    public ResponseEntity<Map<String, String>> createSubscription(@RequestBody Map<String, Object> body, Principal principal) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        String username = principal.getName();
        String priceID = body.get("priceId").toString();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPrice(priceID)
                                .setQuantity(1L)
                                .build()
                )
                .putMetadata("username", username)
                .setSubscriptionData(
                        SessionCreateParams.SubscriptionData.builder()
                                .putMetadata("username", username)
                                .build()
                )
                .setSuccessUrl("https://sahara.andytang.tech/oasis?success=true")
                .setCancelUrl("https://sahara.andytang.tech/oasis?success=false")
                .build();

        Session session = Session.create(params);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("url", session.getUrl()));
    }
}
