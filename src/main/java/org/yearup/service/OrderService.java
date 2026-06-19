package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.*;
import org.yearup.repository.OrderLineItemRepository;
import org.yearup.repository.OrderRepository;
import org.yearup.repository.ShoppingCartRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final ShoppingCartService shoppingCartService;

    public OrderService(OrderRepository orderRepository, OrderLineItemRepository orderLineItemRepository, ShoppingCartService shoppingCartService) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.shoppingCartService = shoppingCartService;
    }

    public Order checkout(int userId){
        ShoppingCart orderCart = shoppingCartService.getByUserId(userId);
        Order order = new Order();

        order.setUserId(userId);
        order.setDate(LocalDate.now());
        order.setShippingAmount(BigDecimal.valueOf(0));

        Order saved = orderRepository.save(order);

        for(ShoppingCartItem item: orderCart.getItems().values()){
            int productId = item.getProductId();
            Product product = item.getProduct();

           OrderLineItem newItem = new OrderLineItem();
           newItem.setOrderId(saved.getOrderId());
           newItem.setProductId(item.getProductId());
           newItem.setQuantity(item.getQuantity());
           newItem.setSalesPrice(BigDecimal.valueOf(item.getLineTotal()));

           orderLineItemRepository.save(newItem);

        }
        return saved;
    }
}
