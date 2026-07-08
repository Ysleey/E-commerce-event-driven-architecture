package com.ecommerce.order.ports.out;

import java.util.List;
import java.util.Optional; // ¡Nuevo import para las listas!

import com.ecommerce.order.domain.model.Order;

public interface OrderRepositoryPort {
    
    // Operaciones principales de persistencia
    Order save(Order order); // Sirve tanto para crear como para actualizar
    void deleteById(Long id); // Más limpio y eficiente que pasarle el objeto completo
    
    // Búsquedas individuales únicas
    Optional<Order> findById(Long id);
    Optional<Order> findByOrderNumber(String orderNumber);
    Optional<Order> findByTrackingNumber(String trackingNumber);
    Optional<Order> findByInvoiceNumber(String invoiceNumber);
    
    // Búsqueda de múltiples registros
    List<Order> findByCustomerId(Long customerId); // Cambiado a List porque un cliente tiene muchos pedidos
}