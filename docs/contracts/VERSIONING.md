# Versionado de contratos API y eventos

## Objetivo

Establecer reglas consistentes para evolucionar contratos REST y eventos Kafka sin romper consumidores ni integraciones existentes.

## Principios

- Cada contrato debe tener una version explicita.
- Los cambios compatibles se entregan como extensiones no rompientes.
- Los cambios incompatibles requieren version nueva y periodo de convivencia.
- La documentacion debe dejar claro el contrato vigente y el legado.

## Versionado OpenAPI

### Esquema recomendado

- `MAJOR.MINOR.PATCH` en `info.version`.
- `MAJOR`: cambios incompatibles.
- `MINOR`: nuevos campos o endpoints compatibles.
- `PATCH`: correcciones documentales o ajustes no funcionales.

### Regla practica

- No eliminar campos obligatorios en la misma version.
- No cambiar semantica de un campo sin version nueva.
- Marcar deprecaciones antes de retirar operaciones.

## Versionado de eventos Kafka

### Esquema recomendado

- `eventVersion` dentro del envelope, por ejemplo `1.0`.
- Mantener `eventType` estable y versionar el payload si el cambio es incompatible.
- Publicar nuevos topics o nuevas versiones de envelope cuando exista ruptura.

### Reglas operativas

- El consumidor debe soportar extensiones de payload.
- El productor debe conservar compatibilidad hacia atras durante la ventana de migracion.
- El campo `correlationId` debe preservarse en todas las versiones.

## Changelog de contratos

- `docs/contracts/CHANGELOG.md` registra la evolucion de versiones y decisiones.
- `docs/contracts/order-service-openapi-v1.yaml` es la fuente de verdad del contrato REST actual.
- `docs/contracts/kafka-events-v1.md` es la fuente de verdad del contrato de eventos actual.
