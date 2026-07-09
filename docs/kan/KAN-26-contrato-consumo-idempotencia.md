# KAN-26 - Contrato de consumo OrderCreated + idempotencia

## Objetivo

Formalizar e implementar el contrato de consumo en shipping-service para `OrderCreated` y garantizar procesamiento idempotente por `eventId`.

## Alcance implementado

1. Consumer base de `ecommerce.order.events.v1` limitado al contrato `OrderCreated` v1.
2. Validacion de contrato antes de procesar mensaje.
3. Idempotencia persistente por `event_id + consumer_name`.
4. Registro de duplicados para evitar doble procesamiento.

## Reglas de contrato aplicadas

El listener de shipping-service acepta solo eventos que cumplan:

1. `eventId` no vacio.
2. `eventType = OrderCreated`.
3. `eventVersion` major `1.x`.
4. `payload.orderId` informado.

Mensajes que no cumplen se registran como rechazados por contrato.

## Estrategia de idempotencia

Tabla de control:

```sql
processed_kafka_events (
  id BIGSERIAL PK,
  event_id VARCHAR(100) NOT NULL,
  consumer_name VARCHAR(100) NOT NULL,
  processed_at TIMESTAMP NOT NULL,
  UNIQUE(event_id, consumer_name)
)
```

Flujo:

1. Parsear mensaje.
2. Validar contrato.
3. Intentar registrar `eventId` para `consumer_name`.
4. Si ya existe o hay colision concurrente: marcar como duplicado y omitir procesamiento.
5. Si es primer procesamiento: continuar al flujo de negocio (KAN-16).

## Evidencia tecnica

Archivos principales:

- `shipping-service/src/main/java/com/ecommerce/shipping/adapter/in/messaging/OrderEventsConsumer.java`
- `shipping-service/src/main/java/com/ecommerce/shipping/application/service/KafkaMessageDeduplicationService.java`
- `shipping-service/src/main/java/com/ecommerce/shipping/adapter/out/persistence/ProcessedKafkaEventEntity.java`
- `shipping-service/src/main/java/com/ecommerce/shipping/adapter/out/persistence/ProcessedKafkaEventJpaRepository.java`
- `shipping-service/src/main/java/com/ecommerce/shipping/adapter/out/persistence/ProcessedKafkaEventPersistenceAdapter.java`

Pruebas:

- `shipping-service/src/test/java/com/ecommerce/shipping/application/service/KafkaMessageDeduplicationServiceTest.java`

## Definition of Done

- Contrato de consumo explicitamente validado en codigo.
- Dedupe persistente activo por `eventId`.
- Duplicados detectados y omitidos sin error de negocio.
- Tests del servicio de idempotencia en verde.
