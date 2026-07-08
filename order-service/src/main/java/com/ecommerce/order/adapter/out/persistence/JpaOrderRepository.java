package com.ecommerce.order.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderRepository extends JpaRepository<OrderEntity, Long> {
    // Spring Data se encargará de implementar la lógica en tiempo de ejecución
    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    Optional<OrderEntity> findByTrackingNumber(String trackingNumber);

    Optional<OrderEntity> findByInvoiceNumber(String invoiceNumber);

    List<OrderEntity> findByCustomerId(Long customerId);
}