package com.ecommerce.order.ports.out;

import java.util.List;

import com.ecommerce.order.domain.model.Order;

public interface OrderListRepositoryPort {
    List<Order> findByCustomerId(Long customerId);
}