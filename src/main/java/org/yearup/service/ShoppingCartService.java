package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.CartItem;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.ShoppingCartRepository;

import java.util.List;

@Service
public class ShoppingCartService
{
    // a shopping cart is built from cart rows plus a product lookup for each row
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService)
    {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }

    public ShoppingCart getByUserId(int userId)
    {
        // load the user's cart rows, look up each product, and build the ShoppingCart
        List<CartItem> cartItems = shoppingCartRepository.findByUserId(userId);
        ShoppingCart shoppingCart = new ShoppingCart();


        for(CartItem item: cartItems){
            int productId = item.getProductId();
            Product product = productService.getById(productId);

            ShoppingCartItem cartItem = new ShoppingCartItem();

            cartItem.setProduct(product);
            cartItem.setQuantity(item.getQuantity());

            shoppingCart.add(cartItem);

        }
        return shoppingCart;
    }


    // add additional methods here
}
