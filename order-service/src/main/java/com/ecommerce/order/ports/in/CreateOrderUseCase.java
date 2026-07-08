package com.ecommerce.order.ports.in;

import com.ecommerce.order.domain.model.Order;

public interface CreateOrderUseCase {
    //Flujo de Checkout/Compras
    Order createOrder(Order order);
}