# KAN-23 - Contrato de eventos Kafka v1 (schemas y versionado)

## Objetivo
Definir contrato de eventos de pedidos para integración entre microservicios con versionado, metadata estándar, estrategia de key y compatibilidad.

## Artefacto principal
- docs/contracts/kafka-events-v1.md

## Alcance cubierto
- Envelope estándar con metadata:
  - eventId
  - eventType
  - eventVersion
  - occurredAt
  - producer
  - traceId
  - correlationId
  - partitionKey
  - payload
- Convenciones de naming de tópicos.
- Tabla de tópicos y eventos.
- Matriz de compatibilidad entre versiones.
- Estrategia de evolución de schema (1.x compatible, 2.x breaking).
- Regla de idempotencia del consumer por eventId.

## Eventos mínimos definidos
- OrderCreated
- OrderStatusChanged (canónico)
- OrderCancelled

## Eventos especializados definidos
- OrderShipped
- OrderCompleted
- OrderReturnRequested
- OrderRefundRequested

## Criterios de aceptación cubiertos
1. Eventos mínimos definidos.
2. Metadata estándar explícita y documentada.
3. Estrategia de versionado y compatibilidad documentada.
4. Convenciones de key/partición documentadas.
5. Contrato listo para consumo por shipping-service.

## Paso a paso de validación
1. Verificar estructura del envelope en contrato.
2. Verificar topic naming y tabla topic-event.
3. Verificar ejemplos JSON de payload por evento.
4. Verificar matriz de compatibilidad.
5. Verificar regla de deduplicación para consumidor.

## Evidencia recomendada para Jira
- Documento de schemas v1.
- Tabla de tópicos y eventos.
- Matriz de compatibilidad.

## Definition of Done
- Contrato v1 aprobado y publicado.
- Reglas de evolución documentadas.
- Riesgos de duplicado/ruptura mitigados a nivel de contrato.
