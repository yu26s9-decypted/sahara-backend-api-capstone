package org.yearup.controllers;

import com.stripe.model.Event;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.repository.ProfileRepository;
import org.yearup.service.UserService;

@RestController
@RequestMapping("/payment")
@CrossOrigin
public class WebhookController {

    @Value("${stripe.webhook.secret}")
    private String whSecret;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/webhook")
    public ResponseEntity<String> webhookHandler(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ){
        try {
            Event e = Webhook.constructEvent(payload, sigHeader, whSecret);

            if("checkout.session.completed".equals(e.getType())) {
                Session session = (Session) e.getDataObjectDeserializer()
                        .deserializeUnsafe();


                String username = session.getMetadata().get("username");
                User user = userService.getByUserName(username);
                Profile profile = profileRepository.findById(user.getId())
                        .orElseThrow();

                profile.setOasis(true);
                profileRepository.save(profile);
            }

            if("customer.subscription.deleted".equals(e.getType())){
                Subscription subscription = (Subscription) e.getDataObjectDeserializer()
                        .deserializeUnsafe();

                String username = subscription.getMetadata().get("username");
                User user = userService.getByUserName(username);
                Profile profile = profileRepository.findById(user.getId())
                        .orElseThrow();

                profile.setOasis(false);
                profileRepository.save(profile);

            }


            return ResponseEntity.status(HttpStatus.OK).body("Received");
        } catch (Exception e) {
            System.out.println("WEBHOOK ERROR: " + e.getMessage());


            return ResponseEntity.badRequest().body("Webhook error: " + e.getMessage());
        }
    }

}
