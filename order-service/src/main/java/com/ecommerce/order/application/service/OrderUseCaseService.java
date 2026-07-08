package com.ecommerce.order.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ecommerce.order.adapter.in.observability.CorrelationContext;
import com.ecommerce.order.adapter.out.messaging.event.OrderEventType;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.model.ShippingInfo;
import com.ecommerce.order.ports.in.CreateOrderUseCase;
import com.ecommerce.order.ports.in.GetOrderUseCase;
import com.ecommerce.order.ports.in.ManagePostSaleUseCase;
import com.ecommerce.order.ports.in.UpdateOrderDetailsUseCase;
import com.ecommerce.order.ports.in.UpdateOrderStateUseCase;
import com.ecommerce.order.ports.out.OrderEventPublisherPort;
import com.ecommerce.order.ports.out.OrderListRepositoryPort;
import com.ecommerce.order.ports.out.OrderQueryRepositoryPort;
import com.ecommerce.order.ports.out.OrderRepositoryPort;

@Service
public class OrderUseCaseService
        implements CreateOrderUseCase, GetOrderUseCase, UpdateOrderDetailsUseCase, UpdateOrderStateUseCase, ManagePostSaleUseCase {

    private final OrderRepositoryPort repositoryPort;
    private final OrderQueryRepositoryPort queryRepositoryPort;
    private final OrderListRepositoryPort listRepositoryPort;
    private final OrderEventPublisherPort eventPublisherPort;

    public OrderUseCaseService(OrderRepositoryPort repositoryPort,
            OrderQueryRepositoryPort queryRepositoryPort,
            OrderListRepositoryPort listRepositoryPort,
            OrderEventPublisherPort eventPublisherPort) {
        this.repositoryPort = repositoryPort;
        this.queryRepositoryPort = queryRepositoryPort;
        this.listRepositoryPort = listRepositoryPort;
        this.eventPublisherPort = eventPublisherPort;
    }

    @Override
    public Order createOrder(Order order) {
        if (order.getOrderNumber() == null || order.getOrderNumber().isBlank()) {
            order.setOrderNumber("ORD-" + UUID.randomUUID());
        }

        if (order.getStatus() == null || order.getStatus().isBlank()) {
            order.setStatus("CREATED");
        }

        LocalDateTime now = LocalDateTime.now();
        if (order.getCreatedAt() == null) {
            order.setCreatedAt(now);
        }
        order.setUpdatedAt(now);

        Order saved = repositoryPort.save(order);
        eventPublisherPort.publishOrderEvent(OrderEventType.ORDER_CREATED, saved, null, resolveCorrelationId(saved));
        return saved;
    }

    @Override
    public Optional<Order> getById(Long id) {
        return queryRepositoryPort.findById(id);
    }

    @Override
    public Optional<Order> getByOrderNumber(String orderNumber) {
        return queryRepositoryPort.findByOrderNumber(orderNumber);
    }

    @Override
    public Optional<Order> getByTrackingNumber(String trackingNumber) {
        return queryRepositoryPort.findByTrackingNumber(trackingNumber);
    }

    @Override
    public List<Order> getByCustomerId(Long customerId) {
        return listRepositoryPort.findByCustomerId(customerId);
    }

    @Override
    public Order updateShippingAddress(Long orderId, String newAddress) {
        Order order = loadOrThrow(orderId);

        ShippingInfo shippingInfo = order.getShippingInfo();
        if (shippingInfo == null) {
            shippingInfo = new ShippingInfo();
            order.setShippingInfo(shippingInfo);
        }

        shippingInfo.setShippingAddress(newAddress);
        order.setUpdatedAt(LocalDateTime.now());
        Order saved = repositoryPort.save(order);
        eventPublisherPort.publishOrderEvent(OrderEventType.ORDER_SHIPPING_ADDRESS_UPDATED, saved, null,
            resolveCorrelationId(saved));
        return saved;
    }

    @Override
    public Order shipOrder(Long orderId, String trackingNumber) {
        Order order = loadOrThrow(orderId);

        ShippingInfo shippingInfo = order.getShippingInfo();
        if (shippingInfo == null) {
            shippingInfo = new ShippingInfo();
            order.setShippingInfo(shippingInfo);
        }

        shippingInfo.setTrackingNumber(trackingNumber);
        order.setStatus("SHIPPED");
        order.setUpdatedAt(LocalDateTime.now());
        Order saved = repositoryPort.save(order);
        eventPublisherPort.publishOrderEvent(OrderEventType.ORDER_SHIPPED, saved, null, resolveCorrelationId(saved));
        return saved;
    }

    @Override
    public Order completeOrder(Long orderId) {
        Order order = loadOrThrow(orderId);
        order.setStatus("COMPLETED");
        order.setUpdatedAt(LocalDateTime.now());
        Order saved = repositoryPort.save(order);
        eventPublisherPort.publishOrderEvent(OrderEventType.ORDER_COMPLETED, saved, null, resolveCorrelationId(saved));
        return saved;
    }

    @Override
    public Order cancelOrder(Long orderId, String reason) {
        Order order = loadOrThrow(orderId);
        order.setStatus("CANCELLED");
        order.setNotes(reason);
        order.setUpdatedAt(LocalDateTime.now());
        Order saved = repositoryPort.save(order);
        eventPublisherPort.publishOrderEvent(OrderEventType.ORDER_CANCELLED, saved, reason, resolveCorrelationId(saved));
        return saved;
    }

    @Override
    public Order returnOrder(Long orderId, String reason) {
        Order order = loadOrThrow(orderId);
        order.setStatus("RETURN_REQUESTED");
        order.setNotes(reason);
        order.setUpdatedAt(LocalDateTime.now());
        Order saved = repositoryPort.save(order);
        eventPublisherPort.publishOrderEvent(OrderEventType.ORDER_RETURN_REQUESTED, saved, reason,
            resolveCorrelationId(saved));
        return saved;
    }

    @Override
    public Order refundOrder(Long orderId) {
        Order order = loadOrThrow(orderId);
        order.setStatus("REFUND_REQUESTED");
        order.setUpdatedAt(LocalDateTime.now());
        Order saved = repositoryPort.save(order);
        eventPublisherPort.publishOrderEvent(OrderEventType.ORDER_REFUND_REQUESTED, saved, null,
                resolveCorrelationId(saved));
        return saved;
    }

    private String resolveCorrelationId(Order order) {
        Optional<String> requestCorrelationId = CorrelationContext.getCorrelationId();
        if (requestCorrelationId.isPresent() && !requestCorrelationId.get().isBlank()) {
            return requestCorrelationId.get();
        }
        if (order.getOrderNumber() != null && !order.getOrderNumber().isBlank()) {
            return order.getOrderNumber();
        }
        if (order.getId() != null) {
            return "ORDER-" + order.getId();
        }
        return UUID.randomUUID().toString();
    }

    private Order loadOrThrow(Long orderId) {
        return queryRepositoryPort.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found for id=" + orderId));
    }
}