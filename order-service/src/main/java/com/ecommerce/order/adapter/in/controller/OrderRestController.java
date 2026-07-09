package com.ecommerce.order.adapter.in.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.order.adapter.in.controller.dto.CancelOrderRequest;
import com.ecommerce.order.adapter.in.controller.dto.CreateOrderRequest;
import com.ecommerce.order.adapter.in.controller.dto.OrderResponse;
import com.ecommerce.order.adapter.in.controller.dto.ReasonRequest;
import com.ecommerce.order.adapter.in.controller.dto.ShipOrderRequest;
import com.ecommerce.order.adapter.in.controller.dto.UpdateShippingAddressRequest;
import com.ecommerce.order.adapter.in.controller.mapper.OrderApiMapper;
import com.ecommerce.order.adapter.in.observability.CorrelationContext;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.ports.in.CreateOrderUseCase;
import com.ecommerce.order.ports.in.GetOrderUseCase;
import com.ecommerce.order.ports.in.ManagePostSaleUseCase;
import com.ecommerce.order.ports.in.UpdateOrderDetailsUseCase;
import com.ecommerce.order.ports.in.UpdateOrderStateUseCase;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderRestController.class);

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final UpdateOrderDetailsUseCase updateOrderDetailsUseCase;
    private final UpdateOrderStateUseCase updateOrderStateUseCase;
    private final ManagePostSaleUseCase managePostSaleUseCase;
    private final OrderApiMapper mapper;

    public OrderRestController(CreateOrderUseCase createOrderUseCase,
            GetOrderUseCase getOrderUseCase,
            UpdateOrderDetailsUseCase updateOrderDetailsUseCase,
            UpdateOrderStateUseCase updateOrderStateUseCase,
            ManagePostSaleUseCase managePostSaleUseCase,
            OrderApiMapper mapper) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
        this.updateOrderDetailsUseCase = updateOrderDetailsUseCase;
        this.updateOrderStateUseCase = updateOrderStateUseCase;
        this.managePostSaleUseCase = managePostSaleUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        String correlationId = CorrelationContext.getCorrelationId().orElse("N/A");
        LOGGER.info("Create order request orderNumber={} customerId={} correlationId={}",
            request.getOrderNumber(),
            request.getCustomerId(),
            correlationId);

        Order created = createOrderUseCase.createOrder(mapper.toDomain(request));
        LOGGER.info("Create order success orderId={} orderNumber={} correlationId={}",
            created.getId(),
            created.getOrderNumber(),
            correlationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable Long id) {
        LOGGER.info("Get order by id={} correlationId={}",
            id,
            CorrelationContext.getCorrelationId().orElse("N/A"));
        Order order = getOrderUseCase.getById(id)
                .orElseThrow(() -> new java.util.NoSuchElementException("Order not found for id=" + id));
        return ResponseEntity.ok(mapper.toResponse(order));
    }

    @GetMapping("/by-order-number/{orderNumber}")
    public ResponseEntity<OrderResponse> getByOrderNumber(@PathVariable String orderNumber) {
        Order order = getOrderUseCase.getByOrderNumber(orderNumber)
                .orElseThrow(() -> new java.util.NoSuchElementException("Order not found for orderNumber=" + orderNumber));
        return ResponseEntity.ok(mapper.toResponse(order));
    }

    @GetMapping("/by-tracking-number/{trackingNumber}")
    public ResponseEntity<OrderResponse> getByTrackingNumber(@PathVariable String trackingNumber) {
        Order order = getOrderUseCase.getByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new java.util.NoSuchElementException("Order not found for trackingNumber=" + trackingNumber));
        return ResponseEntity.ok(mapper.toResponse(order));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> getByCustomerId(@PathVariable Long customerId) {
        List<OrderResponse> response = getOrderUseCase.getByCustomerId(customerId)
                .stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/shipping-address")
    public ResponseEntity<OrderResponse> updateShippingAddress(@PathVariable Long id,
            @RequestBody UpdateShippingAddressRequest request) {
        Order updated = updateOrderDetailsUseCase.updateShippingAddress(id, request.getNewAddress());
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @PostMapping("/{id}/ship")
    public ResponseEntity<OrderResponse> shipOrder(@PathVariable Long id, @RequestBody ShipOrderRequest request) {
        Order updated = updateOrderStateUseCase.shipOrder(id, request.getTrackingNumber());
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<OrderResponse> completeOrder(@PathVariable Long id) {
        Order updated = updateOrderStateUseCase.completeOrder(id);
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id, @RequestBody CancelOrderRequest request) {
        Order updated = updateOrderStateUseCase.cancelOrder(id, request.getReason());
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<OrderResponse> returnOrder(@PathVariable Long id, @RequestBody ReasonRequest request) {
        Order updated = managePostSaleUseCase.returnOrder(id, request.getReason());
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<OrderResponse> refundOrder(@PathVariable Long id) {
        Order updated = managePostSaleUseCase.refundOrder(id);
        return ResponseEntity.ok(mapper.toResponse(updated));
    }
}