package com.ecommerce.shipping.adapter.in.messaging.event;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderEventPayload {

    private Long orderId;
    private String orderNumber;
    private Long customerId;
    private Long productId;
    private Long paymentId;
    private BigDecimal totalAmount;
    private String currency;
    private String status;
    private String trackingNumber;
    private String shippingMethod;
    private String reason;
}
