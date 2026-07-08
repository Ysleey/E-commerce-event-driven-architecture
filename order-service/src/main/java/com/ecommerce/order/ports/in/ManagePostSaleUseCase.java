package com.ecommerce.order.ports.in;

import com.ecommerce.order.domain.model.Order;

public interface ManagePostSaleUseCase {
    //Devoluciones y Reembolsos
    Order returnOrder(Long orderId, String reason);
    Order refundOrder(Long orderId);
}
