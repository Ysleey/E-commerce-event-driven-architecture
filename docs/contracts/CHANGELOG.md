# Changelog de contratos

## [1.0.0] - 2026-07-10

### Added

- Primer contrato OpenAPI del `order-service`.
- Primer contrato de eventos Kafka v1.
- Envelope de evento con `eventId`, `eventType`, `eventVersion`, `occurredAt`, `producer`, `traceId`, `correlationId`, `partitionKey` y `payload`.

### Notes

- Version inicial considerada estable para la iteracion actual del portafolio.
- Cualquier cambio incompatible debe crear una version nueva y documentarse aqui.
