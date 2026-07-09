# KAN-28 - Publicacion de eventos de shipping

## Objetivo

Publicar eventos de dominio desde `shipping-service` para reflejar la creacion del envio y sus cambios de estado.

## Alcance implementado

1. Publicacion de `ShippingCreated` en `ecommerce.shipping.events.v1`.
2. Publicacion de `ShippingStatusChanged` en `ecommerce.shipping.events.v1`.
3. Propagacion de `correlationId` hacia los eventos salientes.
4. Registro de metadata de publicacion para observabilidad.

## Reglas funcionales

1. Al crear un envio en `PENDING`, se emite un evento de creacion.
2. Al cambiar el estado del envio, se emite un evento de cambio con el estado previo.
3. El `orderId` se conserva como clave funcional del flujo.

## Evidencia tecnica

Archivos principales:

- `shipping-service/src/main/java/com/ecommerce/shipping/adapter/out/messaging/KafkaShippingEventPublisher.java`
- `shipping-service/src/main/java/com/ecommerce/shipping/ports/out/ShippingEventPublisherPort.java`
- `shipping-service/src/main/java/com/ecommerce/shipping/application/service/ShippingUseCaseService.java`
- `shipping-service/src/main/java/com/ecommerce/shipping/adapter/in/messaging/OrderEventsConsumer.java`

Pruebas:

- `shipping-service/src/test/java/com/ecommerce/shipping/application/service/ShippingUseCaseServiceTest.java`
- `shipping-service/src/test/java/com/ecommerce/shipping/adapter/in/messaging/OrderEventsConsumerTest.java`

## Definition of Done

- `ShippingCreated` y `ShippingStatusChanged` quedan definidos y publicados.
- La correlacion de trazas se mantiene entre entrada y salida.
- La suite de shipping-service pasa en verde.