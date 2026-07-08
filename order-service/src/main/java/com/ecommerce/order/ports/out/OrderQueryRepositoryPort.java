package com.ecommerce.order.ports.out;

import java.util.Optional;

import com.ecommerce.order.domain.model.Order;

public interface OrderQueryRepositoryPort {
    Optional<Order> findById(Long id);
    Optional<Order> findByOrderNumber(String orderNumber);
    Optional<Order> findByTrackingNumber(String trackingNumber);
    Optional<Order> findByInvoiceNumber(String invoiceNumber);
}