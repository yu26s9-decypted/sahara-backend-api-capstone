package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.User;
import org.yearup.repository.UserRepository;
import org.yearup.service.CategoryService;
import org.yearup.service.ProductService;
import org.yearup.service.UserService;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/oasis")
@CrossOrigin
public class OasisController {
    private final UserService userService;
    private final UserRepository userRepository;


    // create an Autowired constructor to inject the categoryService and productService
    @Autowired
    public OasisController(UserService userService, UserRepository userRepository){
       this.userService = userService;
       this.userRepository = userRepository;

    }

    private int getUserId(Principal principal){
        String userName = principal.getName();
        User user = userService.getByUserName(userName);

        return user.getId();
    }


    @PreAuthorize("isAuthenticated()")
    @PutMapping("/subscribe")
    public ResponseEntity<User> subscription(Principal principal, @RequestBody Map<String, Boolean> body){
        User user = userRepository.findById(getUserId(principal))
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isOasis = body.getOrDefault("oasis", true);

        user.setOasis(isOasis);

        return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(user));
    }


}
