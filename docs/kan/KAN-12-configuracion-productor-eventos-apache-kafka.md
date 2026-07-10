# KAN-12 - Configuracion del Productor de Eventos Apache Kafka

## Objetivo

Preparar la publicacion de eventos del dominio de pedidos hacia Kafka para integracion asincrona con el resto de servicios.

## Alcance implementado

1. Se configura la conexion a Kafka en `application.yml`.
2. Se define el bean o template productor para serializar eventos JSON.
3. Se publica el evento de salida cuando el pedido se crea correctamente.
4. Se verifica el flujo desde la consola o consumidor de pruebas.

## Evidencia tecnica

- Captura de Jira con la subtarea KAN-12 finalizada.
- DoD visible en Jira: tras crear un pedido exitoso, el evento se lee desde la consola de Kafka.

## Definition of Done

- El backend puede publicar eventos de dominio en Kafka.
- La salida de mensajeria queda lista para consumidores posteriores.
- El flujo de publicacion se puede comprobar en local.