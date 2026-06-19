package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.service.ProfileService;
import org.yearup.service.UserService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("profile")
public class ProfileController {
    private final ProfileService profileService;
    private final UserService userService;

    @Autowired
    public ProfileController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    private int getUserId(Principal principal){
        String userName = principal.getName();
        User user = userService.getByUserName(userName);

        return user.getId();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping()
    public ResponseEntity<Profile> getProfile(Principal principal){
        return ResponseEntity.status(HttpStatus.OK).body(profileService.getByUserId(getUserId(principal)));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping()
    public ResponseEntity<Optional<Profile>> updateProfile(@RequestBody Profile profile, Principal principal){
        return ResponseEntity.status(HttpStatus.OK).body(profileService.update(getUserId(principal), profile));
    }






}
