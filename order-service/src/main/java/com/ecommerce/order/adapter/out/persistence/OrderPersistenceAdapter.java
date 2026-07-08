package com.ecommerce.order.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.model.OrderCustomerContact;
import com.ecommerce.order.domain.model.OrderFinancials;
import com.ecommerce.order.domain.model.ShippingInfo;
import com.ecommerce.order.ports.out.OrderDeleteRepositoryPort;
import com.ecommerce.order.ports.out.OrderListRepositoryPort;
import com.ecommerce.order.ports.out.OrderQueryRepositoryPort;
import com.ecommerce.order.ports.out.OrderRepositoryPort;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class OrderPersistenceAdapter
    implements OrderRepositoryPort, OrderQueryRepositoryPort, OrderListRepositoryPort, OrderDeleteRepositoryPort {

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

        return toDomain(savedEntity);
    }

    // =========================================================================
    // 🛠️ MÉTODOS REQUERIDOS POR EL CONTRATO DE INTERFAZ (Soportes provisionales)
    // =========================================================================

    @Override
    public void deleteById(Long id) {
        jpaOrderRepository.deleteById(id);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return jpaOrderRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Order> findByOrderNumber(String orderNumber) {
        return jpaOrderRepository.findByOrderNumber(orderNumber).map(this::toDomain);
    }

    @Override
    public Optional<Order> findByTrackingNumber(String trackingNumber) {
        return jpaOrderRepository.findByTrackingNumber(trackingNumber).map(this::toDomain);
    }

    @Override
    public Optional<Order> findByInvoiceNumber(String invoiceNumber) {
        return jpaOrderRepository.findByInvoiceNumber(invoiceNumber).map(this::toDomain);
    }

    @Override
    public List<Order> findByCustomerId(Long customerId) {
        return jpaOrderRepository.findByCustomerId(customerId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Order toDomain(OrderEntity entity) {
        return Order.builder()
                .id(entity.getId())
                .customerId(entity.getCustomerId())
                .productId(entity.getProductId())
                .paymentId(entity.getPaymentId())
                .orderNumber(entity.getOrderNumber())
                .invoiceNumber(entity.getInvoiceNumber())
                .status(entity.getStatus())
                .notes(entity.getNotes())
                .customerContact(OrderCustomerContact.builder()
                        .customerName(entity.getCustomerName())
                        .customerEmail(entity.getCustomerEmail())
                        .customerPhone(entity.getCustomerPhone())
                        .build())
                .financials(OrderFinancials.builder()
                        .price(entity.getPrice())
                        .discountAmount(entity.getDiscountAmount())
                        .taxAmount(entity.getTaxAmount())
                        .shippingAmount(entity.getShippingAmount())
                        .totalAmount(entity.getTotalAmount())
                        .couponCode(entity.getCouponCode())
                        .currency(entity.getCurrency())
                        .build())
                .shippingInfo(ShippingInfo.builder()
                        .paymentMethod(entity.getPaymentMethod())
                        .shippingMethod(entity.getShippingMethod())
                        .trackingNumber(entity.getTrackingNumber())
                        .shippingAddress(entity.getShippingAddress())
                        .billingAddress(entity.getBillingAddress())
                        .build())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}