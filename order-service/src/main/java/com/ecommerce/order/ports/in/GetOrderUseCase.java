package com.ecommerce.order.ports.in;

import java.util.List;
import java.util.Optional;

import com.ecommerce.order.domain.model.Order;

public interface GetOrderUseCase {
    //Consultas y Rastreo del Cliente
    Optional<Order> getById(Long id);
    Optional<Order> getByOrderNumber(String orderNumber);
    Optional<Order> getByTrackingNumber(String trackingNumber);
    List<Order> getByCustomerId(Long customerId);
}
