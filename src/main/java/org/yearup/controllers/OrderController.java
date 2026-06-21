package org.yearup.controllers;

import org.apache.coyote.Response;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Order;
import org.yearup.models.OrderLineItem;
import org.yearup.models.User;
import org.yearup.service.OrderService;
import org.yearup.service.UserService;

import java.security.Principal;
import java.util.List;

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
    public ResponseEntity<Order> createOrder(Principal principal){
        try {
            Order order = orderService.checkout(getUserId(principal));
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<List<Order>> getOrders(Principal principal){
       List<Order> order = orderService.getByUserId(getUserId(principal));
       return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderLineItem>> getOrderLineItems(@PathVariable int orderId){
        List<OrderLineItem> orderLineItems = orderService.getLineItems(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(orderLineItems);
    }


}
