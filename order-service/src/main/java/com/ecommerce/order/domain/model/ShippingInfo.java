package com.ecommerce.order.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingInfo {
    private String paymentMethod;
    private String shippingMethod;
    private String trackingNumber;
    private String shippingAddress;
    private String billingAddress;
}