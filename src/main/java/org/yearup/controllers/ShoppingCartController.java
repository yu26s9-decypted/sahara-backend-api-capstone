package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;
import org.yearup.service.CategoryService;
import org.yearup.service.ProductService;
import org.yearup.service.ShoppingCartService;
import org.yearup.service.UserService;

import java.security.Principal;

// convert this class to a REST controller
// only logged in users should have access to these actions
@RestController
@RequestMapping("cart")
@CrossOrigin
public class ShoppingCartController
{
    // a shopping cart controller depends on the service layer
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService){
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }



    // I realize if I continue to implement postCart following the same pattern was getCart, this will violate DRY principle so i made a
    // helper method for it!

    private int getUserId(Principal principal){
        String userName = principal.getName();
        User user = userService.getByUserName(userName);

        return user.getId();
    }
    // each method in this controller requires a Principal object as a parameter
    @PreAuthorize("isAuthenticated()")
    @GetMapping()
    public ShoppingCart getCart(Principal principal)
    {

        return shoppingCartService.getByUserId(getUserId(principal));
    }

    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15  (15 is the productId to be added)
    // return the updated cart with status 201 Created

    @PostMapping("/products/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ShoppingCart> createCart(@PathVariable int productId, Principal principal){
       return null; // to complete when we implement service layer
    }


    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15  (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated; return the cart (200 OK)

    @PutMapping("/products/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ShoppingCart> updateCart(@PathVariable int productId, Principal principal){
        return null; // to complete when we implement service layer
    }


    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart  - return the (now empty) cart so the front end can refresh it (200 OK)
    @DeleteMapping("/products")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ShoppingCart> deleteCart(Principal principal){
        return null;
    }
}
