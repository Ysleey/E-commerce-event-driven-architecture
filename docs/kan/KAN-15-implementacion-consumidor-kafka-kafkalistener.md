# KAN-15 - Implementacion del Consumidor Kafka (@KafkaListener)

## Objetivo

Implementar la entrada asincrona de `shipping-service` para procesar mensajes de Kafka desde el evento de pedidos.

## Alcance implementado

1. Se configura un listener Kafka concurrente en la infraestructura del servicio.
2. Se consume el topic de eventos de pedidos con un metodo anotado con `@KafkaListener`.
3. Se deserializa el JSON recibido a un DTO local.
4. Se registra el flujo consumido en logs legibles para validacion y trazabilidad.

## Evidencia tecnica

- Captura de Jira con la subtarea KAN-15 finalizada.
- DoD visible en Jira: al recibir un pedido, el servicio captura el evento y muestra los datos en logs de consola.

## Definition of Done

- El consumidor Kafka escucha el topic esperado.
- Los mensajes se procesan en local sin errores de infraestructura.
- La base asincrona queda lista para la logica de negocio y la persistencia.