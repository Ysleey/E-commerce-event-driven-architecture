package com.ecommerce.order.infrastructure.output.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    //Anotaciones de JPA para mapeo de la entidad a la base de datos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private Long productId;
    private Long paymentId;
    
    @Column(unique = true, nullable = false)
    private String orderNumber;
    private String invoiceNumber;
    private String status;
    private String notes;

    // Campos del Objeto de Valor: Contacto
    private String customerName;
    private String customerEmail;
    private String customerPhone;

    // Campos del Objeto de Valor: Financiero
    private BigDecimal price;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal shippingAmount;
    private BigDecimal totalAmount;
    private String couponCode;
    private String currency;

    // Campos del Objeto de Valor: Envío/Logística
    private String paymentMethod;
    private String shippingMethod;
    private String trackingNumber;
    private String shippingAddress;
    private String billingAddress;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}