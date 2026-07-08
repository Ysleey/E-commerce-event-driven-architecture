package com.ecommerce.order.infrastructure.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderRepository extends JpaRepository<OrderEntity, Long> {
    // Spring Data se encargará de implementar la lógica en tiempo de ejecución
}