package com.ecommerce.order.domain.model;

import java.math.BigDecimal;
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
    // 1. Identificadores únicos 
    private Long id;
    private Long customerId; // Con esto el microservicio de Clientes dará su perfil cuando lo necesite
    private Long productId;
    private Long paymentId;
    private String orderNumber;
    private String trackingNumber;
    private String invoiceNumber;

    // 2. Datos del flujo del Pedido
    private Integer quantity;
    private String status;
    private String notes;
    private String currency;

    // 3. Desglose Financiero 
    private BigDecimal price;          // Precio base unitario
    private BigDecimal discountAmount; // Descuento aplicado por el cupón
    private BigDecimal taxAmount;      // Impuestos (IVA/Tasas)
    private BigDecimal shippingAmount; // Coste del envío
    private BigDecimal totalAmount;    // Cálculo final: (precio * cant) + tasas + envío - descuento
    private String couponCode;

    // 4. Logística e Información de contacto rápida para el repartidor
    private String paymentMethod;
    private String shippingMethod;
    private String customerName;       // Nombre de facturación del momento
    private String customerEmail;      // Email para notificar el envío del paquete
    private String customerPhone;      // Teléfono por si el repartidor no encuentra la casa
    private String shippingAddress;    // Dirección física de entrega
    private String billingAddress;     // Dirección fiscal

    // 5. Auditoría de Tiempo
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}