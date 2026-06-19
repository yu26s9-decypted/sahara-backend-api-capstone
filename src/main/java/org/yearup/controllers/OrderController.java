package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Order;
import org.yearup.models.User;
import org.yearup.service.OrderService;
import org.yearup.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;


    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    private int getUserId(Principal principal){
        String userName = principal.getName();
        User user = userService.getByUserName(userName);

        return user.getId();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public Order createOrder(Principal principal){
        System.out.println(orderService.checkout(getUserId(principal)));
        return orderService.checkout(getUserId(principal));
    }
}
