# ADR-001: Arquitectura hexagonal + eventos Kafka para order-service

- Estado: Aprobado
- Fecha: 2026-07-09
- Ambito: `order-service`

## Contexto

El dominio de ordenes requiere:

- evolucion funcional rapida (nuevos estados de negocio),
- integracion asincrona con otros microservicios,
- trazabilidad operativa entre peticiones REST y eventos,
- posibilidad de testear reglas de negocio sin acoplarse a infraestructura.

## Decision

Se adopta una arquitectura hexagonal para `order-service` con:

- `domain` y `application` independientes de frameworks,
- adaptadores de entrada REST/JWT/observabilidad,
- adaptadores de salida JPA/Kafka,
- puertos in/out como contratos explicitos,
- publicacion de eventos de dominio en Kafka v1 incluyendo `correlationId`.

## Consecuencias

### Positivas

- Casos de uso testeables aislando persistencia y mensajeria.
- Cambios de infraestructura con menor impacto en dominio.
- Integracion entre servicios via contratos versionados.
- Mejor soporte operativo por correlacion request-evento.

### Costos

- Mayor numero de clases/abstracciones.
- Curva inicial para nuevos colaboradores.
- Necesidad de disciplina para mantener limites entre capas.

## Alternativas consideradas

1. Arquitectura en capas tradicional (controller-service-repository)
- Rechazada por mayor acoplamiento y menor claridad de fronteras.

2. Publicacion directa desde controlador
- Rechazada por mezclar preocupaciones de transporte con dominio.

## Evidencia de cumplimiento

- Estructura de paquetes por `adapter`, `application`, `domain`, `ports`.
- Publicador Kafka desacoplado via `OrderEventPublisherPort`.
- Correlation ID propagado desde filtro hasta evento publicado.
