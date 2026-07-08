# KAN-25 - Resiliencia producer/consumer (retry y DLQ base)

## Objetivo
Definir e implementar base de resiliencia en mensajería Kafka para evitar pérdida de mensajes, controlar errores y prevenir doble procesamiento de negocio.

## Alcance funcional
1. Producer con reintentos e idempotencia activada.
2. Consumer con política de retry y enrutamiento a DLQ.
3. Estrategia de idempotencia de consumo por eventId persistido.

## Criterios de aceptación
1. Producer y consumer con política explícita de retry.
2. Mensajes no recuperables terminan en DLQ.
3. Reglas de backoff y máximo de reintentos definidas.
4. Comportamiento ante fallos de serialización/validación documentado.
5. Idempotencia por consumer definida e implementable.

## Diseño recomendado (paso a paso)
1. Producer:
   - acks=all
   - enable.idempotence=true
   - retries>0
   - delivery timeout y logs de éxito/error
2. Consumer:
   - Retry topic o DefaultErrorHandler con backoff exponencial.
   - DLT topic por dominio (ej: ecommerce.order.events.v1.dlq).
3. Idempotencia:
   - Tabla processed_kafka_events con PK event_id.
   - Si event_id existe: skip de negocio + commit offset.
   - Si no existe: procesar, guardar event_id, commit.
4. Pruebas:
   - Caso de fallo temporal con reintento.
   - Caso no recuperable enviado a DLQ.
   - Caso de evento duplicado no reprocesado.

## Evidencia requerida para Jira
- Ejemplo de mensaje en DLQ.
- Registro de reintentos en logs.
- Resultado de prueba de idempotencia.
- Documento corto de política de resiliencia aplicada.

## Definition of Done
- Flujo de error reproducible validado.
- DLQ comprobada con caso real.
- Operación local estable ante fallos controlados.
