package com.ecommerce.order.infrastructure.output.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.model.OrderCustomerContact;
import com.ecommerce.order.domain.model.OrderFinancials;
import com.ecommerce.order.domain.model.ShippingInfo;
import com.ecommerce.order.infrastructure.output.persistence.JpaOrderRepository;
import com.ecommerce.order.infrastructure.output.persistence.OrderEntity;
import com.ecommerce.order.ports.out.OrderRepositoryPort;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements OrderRepositoryPort {

    private final JpaOrderRepository jpaOrderRepository;

    @Override
    public Order save(Order order) {
        OrderEntity entity = OrderEntity.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .productId(order.getProductId())
                .paymentId(order.getPaymentId())
                .orderNumber(order.getOrderNumber())
                .invoiceNumber(order.getInvoiceNumber())
                .status(order.getStatus())
                .notes(order.getNotes())
                .customerName(order.getCustomerContact() != null ? order.getCustomerContact().getCustomerName() : null)
                .customerEmail(order.getCustomerContact() != null ? order.getCustomerContact().getCustomerEmail() : null)
                .customerPhone(order.getCustomerContact() != null ? order.getCustomerContact().getCustomerPhone() : null)
                .price(order.getFinancials() != null ? order.getFinancials().getPrice() : null)
                .discountAmount(order.getFinancials() != null ? order.getFinancials().getDiscountAmount() : null)
                .taxAmount(order.getFinancials() != null ? order.getFinancials().getTaxAmount() : null)
                .shippingAmount(order.getFinancials() != null ? order.getFinancials().getShippingAmount() : null)
                .totalAmount(order.getFinancials() != null ? order.getFinancials().getTotalAmount() : null)
                .couponCode(order.getFinancials() != null ? order.getFinancials().getCouponCode() : null)
                .currency(order.getFinancials() != null ? order.getFinancials().getCurrency() : null)
                .paymentMethod(order.getShippingInfo() != null ? order.getShippingInfo().getPaymentMethod() : null)
                .shippingMethod(order.getShippingInfo() != null ? order.getShippingInfo().getShippingMethod() : null)
                .trackingNumber(order.getShippingInfo() != null ? order.getShippingInfo().getTrackingNumber() : null)
                .shippingAddress(order.getShippingInfo() != null ? order.getShippingInfo().getShippingAddress() : null)
                .billingAddress(order.getShippingInfo() != null ? order.getShippingInfo().getBillingAddress() : null)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();

        OrderEntity savedEntity = jpaOrderRepository.save(entity);

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

    // =========================================================================
    // 🛠️ MÉTODOS REQUERIDOS POR EL CONTRATO DE INTERFAZ (Soportes provisionales)
    // =========================================================================

    public void deleteById(Long id) {
        jpaOrderRepository.deleteById(id);
    }

   
    public Optional<Order> findById(Long id) {
        throw new UnsupportedOperationException("FindById no implementado aún");
    }

   
    public Optional<Order> findByOrderNumber(String orderNumber) {
        throw new UnsupportedOperationException("FindByOrderNumber no implementado aún");
    }

  
    public Optional<Order> findByTrackingNumber(String trackingNumber) {
        throw new UnsupportedOperationException("FindByTrackingNumber no implementado aún");
    }

 
    public Optional<Order> findByInvoiceNumber(String invoiceNumber) {
        throw new UnsupportedOperationException("FindByInvoiceNumber no implementado aún");
    }

    
    public List<Order> findByCustomerId(Long customerId) {
        throw new UnsupportedOperationException("FindByCustomerId no implementado aún");
    }
}