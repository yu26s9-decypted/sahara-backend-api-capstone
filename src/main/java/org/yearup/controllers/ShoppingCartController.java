package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.*;
import org.yearup.service.CategoryService;
import org.yearup.service.ProductService;
import org.yearup.service.ShoppingCartService;
import org.yearup.service.UserService;

import java.security.Principal;
import java.util.List;

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
    public ResponseEntity<ShoppingCart> addToCart(@PathVariable int productId, Principal principal){
      return ResponseEntity.status(HttpStatus.CREATED).body(shoppingCartService.addItem(getUserId(principal), productId));
    }


    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15  (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated; return the cart (200 OK)

    @PutMapping("/products/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ShoppingCart> updateCart(@RequestBody ShoppingCartItem shoppingCartItem, @PathVariable int productId, Principal principal){
        return ResponseEntity.status(HttpStatus.OK).body(shoppingCartService.updateItem(getUserId(principal), productId, shoppingCartItem.getQuantity()));

    }

    @DeleteMapping("/products/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ShoppingCart> deleteItemFromCart( @PathVariable int productId, Principal principal){
        ShoppingCart removed = shoppingCartService.deleteItem(getUserId(principal), productId);
        return ResponseEntity.status(HttpStatus.OK).body(removed);
    }



    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart  - return the (now empty) cart so the front end can refresh it (200 OK)
    @DeleteMapping("")
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ResponseEntity<ShoppingCart> clearCart(Principal principal){
        return ResponseEntity.status(HttpStatus.OK).body(shoppingCartService.clearCart(getUserId(principal)));
    }
}
