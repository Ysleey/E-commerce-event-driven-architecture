package com.ecommerce.order.adapter.in.controller.mapper;

import org.springframework.stereotype.Component;

import com.ecommerce.order.adapter.in.controller.dto.CreateOrderRequest;
import com.ecommerce.order.adapter.in.controller.dto.OrderResponse;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.model.OrderCustomerContact;
import com.ecommerce.order.domain.model.OrderFinancials;
import com.ecommerce.order.domain.model.ShippingInfo;

@Component
public class OrderApiMapper {

    public Order toDomain(CreateOrderRequest request) {
        if (request == null) {
            return null;
        }

        return Order.builder()
                .customerId(request.getCustomerId())
                .productId(request.getProductId())
                .paymentId(request.getPaymentId())
                .orderNumber(request.getOrderNumber())
                .invoiceNumber(request.getInvoiceNumber())
                .notes(request.getNotes())
                .customerContact(toCustomerContact(request.getCustomerContact()))
                .financials(toFinancials(request.getFinancials()))
                .shippingInfo(toShippingInfo(request.getShippingInfo()))
                .build();
    }

    public OrderResponse toResponse(Order order) {
        if (order == null) {
            return null;
        }

        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCustomerId(order.getCustomerId());
        response.setProductId(order.getProductId());
        response.setPaymentId(order.getPaymentId());
        response.setOrderNumber(order.getOrderNumber());
        response.setInvoiceNumber(order.getInvoiceNumber());
        response.setStatus(order.getStatus());
        response.setNotes(order.getNotes());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        response.setCustomerContact(toCustomerContactOutput(order.getCustomerContact()));
        response.setFinancials(toFinancialsOutput(order.getFinancials()));
        response.setShippingInfo(toShippingInfoOutput(order.getShippingInfo()));
        return response;
    }

    private OrderCustomerContact toCustomerContact(CreateOrderRequest.CustomerContactInput input) {
        if (input == null) {
            return null;
        }

        return OrderCustomerContact.builder()
                .customerName(input.getCustomerName())
                .customerEmail(input.getCustomerEmail())
                .customerPhone(input.getCustomerPhone())
                .build();
    }

    private OrderFinancials toFinancials(CreateOrderRequest.FinancialsInput input) {
        if (input == null) {
            return null;
        }

        return OrderFinancials.builder()
                .price(input.getPrice())
                .discountAmount(input.getDiscountAmount())
                .taxAmount(input.getTaxAmount())
                .shippingAmount(input.getShippingAmount())
                .totalAmount(input.getTotalAmount())
                .couponCode(input.getCouponCode())
                .currency(input.getCurrency())
                .build();
    }

    private ShippingInfo toShippingInfo(CreateOrderRequest.ShippingInfoInput input) {
        if (input == null) {
            return null;
        }

        return ShippingInfo.builder()
                .paymentMethod(input.getPaymentMethod())
                .shippingMethod(input.getShippingMethod())
                .trackingNumber(input.getTrackingNumber())
                .shippingAddress(input.getShippingAddress())
                .billingAddress(input.getBillingAddress())
                .build();
    }

    private OrderResponse.CustomerContactOutput toCustomerContactOutput(OrderCustomerContact contact) {
        if (contact == null) {
            return null;
        }

        OrderResponse.CustomerContactOutput output = new OrderResponse.CustomerContactOutput();
        output.setCustomerName(contact.getCustomerName());
        output.setCustomerEmail(contact.getCustomerEmail());
        output.setCustomerPhone(contact.getCustomerPhone());
        return output;
    }

    private OrderResponse.FinancialsOutput toFinancialsOutput(OrderFinancials financials) {
        if (financials == null) {
            return null;
        }

        OrderResponse.FinancialsOutput output = new OrderResponse.FinancialsOutput();
        output.setPrice(financials.getPrice());
        output.setDiscountAmount(financials.getDiscountAmount());
        output.setTaxAmount(financials.getTaxAmount());
        output.setShippingAmount(financials.getShippingAmount());
        output.setTotalAmount(financials.getTotalAmount());
        output.setCouponCode(financials.getCouponCode());
        output.setCurrency(financials.getCurrency());
        return output;
    }

    private OrderResponse.ShippingInfoOutput toShippingInfoOutput(ShippingInfo shippingInfo) {
        if (shippingInfo == null) {
            return null;
        }

        OrderResponse.ShippingInfoOutput output = new OrderResponse.ShippingInfoOutput();
        output.setPaymentMethod(shippingInfo.getPaymentMethod());
        output.setShippingMethod(shippingInfo.getShippingMethod());
        output.setTrackingNumber(shippingInfo.getTrackingNumber());
        output.setShippingAddress(shippingInfo.getShippingAddress());
        output.setBillingAddress(shippingInfo.getBillingAddress());
        return output;
    }
}