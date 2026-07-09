# KAN-29 - Pruebas de integracion con Kafka local

## Objetivo

Validar en entorno local la interoperabilidad entre `order-service` y `shipping-service` sobre Kafka KRaft de 3 brokers.

## Alcance implementado

1. Flujo exitoso `OrderCreated -> ShippingCreated`.
2. Flujo de duplicados sin doble creacion de envio.
3. Flujo de error con redireccion a DLQ.
4. Topics de prueba aislados del entorno interactivo.

## Evidencia tecnica

Archivos principales:

- `shipping-service/src/test/java/com/ecommerce/shipping/integration/ShippingKafkaIntegrationTest.java`
- `shipping-service/src/main/java/com/ecommerce/shipping/adapter/out/messaging/KafkaListenerConfig.java`
- `shipping-service/src/main/java/com/ecommerce/shipping/adapter/out/messaging/KafkaTopicsConfig.java`

Validacion ejecutada:

- `shipping-service` `ShippingKafkaIntegrationTest`: 3 tests, 0 failures, 0 errors.

## Definition of Done

- Suite reproducible en local.
- Flujo completo verificado.
- Error y DLQ cubiertos.
- Duplicados verificados.