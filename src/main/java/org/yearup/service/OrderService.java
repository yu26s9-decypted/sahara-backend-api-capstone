package org.yearup.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yearup.models.*;
import org.yearup.repository.OrderLineItemRepository;
import org.yearup.repository.OrderRepository;
import org.yearup.repository.ShoppingCartRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

    @Transactional
    public Order checkout(int userId){
        ShoppingCart orderCart = shoppingCartService.getByUserId(userId);

        if(orderCart.getItems().isEmpty()){
            throw new IllegalStateException("Can't checkout because cart is empty.");
        }

        Order order = new Order();

        order.setUserId(userId);
        order.setDate(LocalDate.now());
        order.setShippingAmount(BigDecimal.valueOf(0));

        Order saved = orderRepository.save(order);

        for(ShoppingCartItem item: orderCart.getItems().values()){
           OrderLineItem newItem = new OrderLineItem();
           newItem.setOrderId(saved.getOrderId());
           newItem.setProductId(item.getProductId());
           newItem.setQuantity(item.getQuantity());
           newItem.setSalesPrice(BigDecimal.valueOf(item.getLineTotal()));

           orderLineItemRepository.save(newItem);

        }


        shoppingCartService.clearCart(userId);
        return saved;
    }

    public List<Order> getByUserId(int userId){
        return orderRepository.findByUserId(userId);
    }

    public List<OrderLineItem> getLineItems(int orderId){
        return orderLineItemRepository.findByOrderId(orderId);
    }
}
