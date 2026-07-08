package com.ecommerce.order.ports.out;

import com.ecommerce.order.domain.model.Order;

public interface OrderRepositoryPort {
    
    // Operaciones principales de persistencia
    Order save(Order order); // Sirve tanto para crear como para actualizar
}