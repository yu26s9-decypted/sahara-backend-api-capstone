package org.yearup.controllers;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeSearchResult;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.*;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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
                .setAutomaticTax(
                        SessionCreateParams.AutomaticTax.builder()
                                .setEnabled(true)
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

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/cancel")
    public ResponseEntity<Map<String, String>> cancelSubscription(Principal principal){
        Stripe.apiKey = stripeApiKey;

        try {
            String username = principal.getName();

            SubscriptionSearchParams params = SubscriptionSearchParams.builder()
                    .setQuery("metadata['username']:'" + username + "'")
                    .setLimit(1L)
                    .build();

            StripeSearchResult<Subscription> result =
                    Subscription.search(params);

            if (result.getData().isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "No subscription found"));
            }

            Subscription subscription = result.getData().get(0);

            SubscriptionUpdateParams updateParams = SubscriptionUpdateParams.builder()
                    .setCancelAtPeriodEnd(true)
                    .build();

            subscription.update(updateParams);

            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "subscription was canceled and will reflect at the end of billign cycle"));

        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "There was an error. " + e.getMessage()));
        }


    }


}
