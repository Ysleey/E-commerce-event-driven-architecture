package com.ecommerce.order.infrastructure.output.persistence.adapter;

import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.model.OrderCustomerContact;
import com.ecommerce.order.domain.model.OrderFinancials;
import com.ecommerce.order.domain.model.ShippingInfo;
import com.ecommerce.order.ports.out.OrderRepositoryPort;
import com.ecommerce.order.infrastructure.output.persistence.JpaOrderRepository;
import com.ecommerce.order.infrastructure.output.persistence.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements OrderRepositoryPort {

    private final JpaOrderRepository jpaOrderRepository;

    @Override
    public Order save(Order order) {
        // 1. Mapeamos de Dominio Puro a Entidad Física de Tabla SQL
        OrderEntity entity = OrderEntity.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .productId(order.getProductId())
                .paymentId(order.getPaymentId())
                .orderNumber(order.getOrderNumber())
                .invoiceNumber(order.getInvoiceNumber())
                .status(order.getStatus())
                .notes(order.getNotes())
                // Bloque contacto
                .customerName(order.getCustomerContact() != null ? order.getCustomerContact().getCustomerName() : null)
                .customerEmail(order.getCustomerContact() != null ? order.getCustomerContact().getCustomerEmail() : null)
                .customerPhone(order.getCustomerContact() != null ? order.getCustomerContact().getCustomerPhone() : null)
                // Bloque financieros
                .price(order.getFinancials() != null ? order.getFinancials().getPrice() : null)
                .discountAmount(order.getFinancials() != null ? order.getFinancials().getDiscountAmount() : null)
                .taxAmount(order.getFinancials() != null ? order.getFinancials().getTaxAmount() : null)
                .shippingAmount(order.getFinancials() != null ? order.getFinancials().getShippingAmount() : null)
                .totalAmount(order.getFinancials() != null ? order.getFinancials().getTotalAmount() : null)
                .couponCode(order.getFinancials() != null ? order.getFinancials().getCouponCode() : null)
                .currency(order.getFinancials() != null ? order.getFinancials().getCurrency() : null)
                // Bloque logística
                .paymentMethod(order.getShippingInfo() != null ? order.getShippingInfo().getPaymentMethod() : null)
                .shippingMethod(order.getShippingInfo() != null ? order.getShippingInfo().getShippingMethod() : null)
                .trackingNumber(order.getShippingInfo() != null ? order.getShippingInfo().getTrackingNumber() : null)
                .shippingAddress(order.getShippingInfo() != null ? order.getShippingInfo().getShippingAddress() : null)
                .billingAddress(order.getShippingInfo() != null ? order.getShippingInfo().getBillingAddress() : null)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();

        // 2. Guardamos en la base de datos
        OrderEntity savedEntity = jpaOrderRepository.save(entity);

        // 3. Re-mapeamos la entidad guardada de vuelta a un objeto de dominio
        return Order.builder()
                .id(savedEntity.getId())
                .customerId(savedEntity.getCustomerId())
                .productId(savedEntity.getProductId())
                .paymentId(savedEntity.getPaymentId())
                .orderNumber(savedEntity.getOrderNumber())
                .invoiceNumber(savedEntity.getInvoiceNumber())
                .status(savedEntity.getStatus())
                .notes(savedEntity.getNotes())
                .customerContact(OrderCustomerContact.builder()
                        .customerName(savedEntity.getCustomerName())
                        .customerEmail(savedEntity.getCustomerEmail())
                        .customerPhone(savedEntity.getCustomerPhone())
                        .build())
                .financials(OrderFinancials.builder()
                        .price(savedEntity.getPrice())
                        .discountAmount(savedEntity.getDiscountAmount())
                        .taxAmount(savedEntity.getTaxAmount())
                        .shippingAmount(savedEntity.getShippingAmount())
                        .totalAmount(savedEntity.getTotalAmount())
                        .couponCode(savedEntity.getCouponCode())
                        .currency(savedEntity.getCurrency())
                        .build())
                .shippingInfo(ShippingInfo.builder()
                        .paymentMethod(savedEntity.getPaymentMethod())
                        .shippingMethod(savedEntity.getShippingMethod())
                        .trackingNumber(savedEntity.getTrackingNumber())
                        .shippingAddress(savedEntity.getShippingAddress())
                        .billingAddress(savedEntity.getBillingAddress())
                        .build())
                .createdAt(savedEntity.getCreatedAt())
                .updatedAt(savedEntity.getUpdatedAt())
                .build();
    }
}