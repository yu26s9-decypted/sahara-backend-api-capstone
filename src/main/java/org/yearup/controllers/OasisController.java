package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.repository.ProfileRepository;
import org.yearup.repository.UserRepository;
import org.yearup.service.CategoryService;
import org.yearup.service.ProductService;
import org.yearup.service.ProfileService;
import org.yearup.service.UserService;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/oasis")
@CrossOrigin
public class OasisController {
    private final ProfileRepository profileRepository;
    private final UserService userService;


    // create an Autowired constructor to inject the categoryService and productService
    @Autowired
    public OasisController(ProfileRepository profileRepository, UserService userService){
       this.profileRepository = profileRepository;
       this.userService = userService;

    }

    private int getUserId(Principal principal){
        String userName = principal.getName();
        User user = userService.getByUserName(userName);

        return user.getId();
    }


    @PreAuthorize("isAuthenticated()")
    @PutMapping("/subscribe")
    public ResponseEntity<Profile> subscribe(Principal principal, @RequestBody Map<String, Boolean> body){
        Profile profile = profileRepository.findById(getUserId(principal))
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isOasis = body.getOrDefault("oasis", true);

        profile.setOasis(isOasis);

        return ResponseEntity.status(HttpStatus.OK).body(profileRepository.save(profile));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/cancel")
    public ResponseEntity<Profile> cancel(Principal principal){
        Profile profile = profileRepository.findById(getUserId(principal))
                .orElseThrow(() -> new RuntimeException("User not found"));


        // could implement a feature where it waits until the persons subscription ran out but for now this
        // for now, cancellation will revoke the membership immediately.
        profile.setOasis(false);

        return ResponseEntity.status(HttpStatus.OK).body(profileRepository.save(profile));
    }

}
