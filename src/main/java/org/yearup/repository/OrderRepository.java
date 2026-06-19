package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yearup.models.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(int userId);
}
