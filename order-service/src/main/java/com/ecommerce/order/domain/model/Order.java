package com.ecommerce.order.domain.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    // 1. Identificadores únicos y estado de control
    private Long id;
    private Long customerId;
    private Long productId;
    private Long paymentId;
    private String orderNumber;
    private String invoiceNumber;
    private String status;
    private String notes;

    // 2. Bloques de información encapsulados (Value Objects)
    private OrderCustomerContact customerContact;
    private OrderFinancials financials;
    private ShippingInfo shippingInfo;

    // 3. Auditoría de tiempos
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}