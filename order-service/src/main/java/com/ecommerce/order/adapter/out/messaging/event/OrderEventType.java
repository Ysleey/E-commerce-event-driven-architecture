package com.ecommerce.order.adapter.out.messaging.event;

public enum OrderEventType {
    ORDER_CREATED("OrderCreated"),
    ORDER_SHIPPING_ADDRESS_UPDATED("OrderShippingAddressUpdated"),
    ORDER_SHIPPED("OrderShipped"),
    ORDER_COMPLETED("OrderCompleted"),
    ORDER_CANCELLED("OrderCancelled"),
    ORDER_RETURN_REQUESTED("OrderReturnRequested"),
    ORDER_REFUND_REQUESTED("OrderRefundRequested");

    private final String value;

    OrderEventType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
