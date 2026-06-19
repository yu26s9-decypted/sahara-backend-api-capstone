package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yearup.models.OrderLineItem;

import java.util.List;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Integer> {
    List<OrderLineItem> findByOrderId(int orderId);
}
