# KAN-22 - Contrato API OpenAPI v1 (order-service)

## Objetivo
Definir y mantener un contrato OpenAPI 3.0 completo y validable para order-service, como fuente de verdad de integración entre backend y frontend.

## Artefacto principal
- docs/contracts/order-service-openapi-v1.yaml

## Alcance cubierto
- Endpoint público de autenticación: POST /api/v1/auth/login.
- Endpoints protegidos de pedidos:
  - POST /api/orders
  - GET /api/orders/{id}
  - GET /api/orders/by-order-number/{orderNumber}
  - GET /api/orders/by-tracking-number/{trackingNumber}
  - GET /api/orders/customer/{customerId}
  - PATCH /api/orders/{id}/shipping-address
  - POST /api/orders/{id}/ship
  - POST /api/orders/{id}/complete
  - POST /api/orders/{id}/cancel
  - POST /api/orders/{id}/return
  - POST /api/orders/{id}/refund

## Criterios de aceptación cubiertos
1. Especificación OpenAPI v1 completa y consistente con el código.
2. Endpoints mínimos de creación, consulta, actualización de estado y postventa definidos.
3. Cada endpoint incluye requestBody/responseBody/códigos HTTP (cuando aplica).
4. Seguridad por endpoint documentada (público/protegido con bearer JWT).
5. Contrato consumible sin ambigüedad para frontend.

## Paso a paso de validación
1. Levantar order-service en local.
2. Verificar que rutas y métodos del controller coinciden con el YAML.
3. Verificar que esquemas LoginRequest, LoginResponse, CreateOrderRequest y OrderResponse reflejan DTOs reales.
4. Verificar códigos HTTP esperados: 200/201/400/401/403/404 según endpoint.
5. Adjuntar evidencia en Jira.

## Evidencia recomendada para Jira
- Archivo OpenAPI v1 actualizado.
- Captura de revisión de métodos/rutas.
- Nota de validación funcional con Postman (login + endpoint protegido).

## Definition of Done
- No endpoint implementado sin reflejo en el contrato.
- No endpoint en contrato sin esquema mínimo de request/response.
- Seguridad público/protegido explícita por endpoint.
