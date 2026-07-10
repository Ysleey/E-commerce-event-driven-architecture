# KAN-14 - Inicializacion y Aislamiento de Entorno de shipping-service

## Objetivo

Crear el proyecto Spring Boot de logistica en un entorno propio, aislado del resto de servicios, para poder trabajar con seguridad y puertos dedicados.

## Alcance implementado

1. Se genera el proyecto base de `shipping-service` con Maven.
2. Se configura el puerto local de escucha en `8082`.
3. Se separa su configuracion de infraestructura para evitar colisiones con `order-service`.
4. Se valida el arranque limpio del servicio en entorno local.

## Evidencia tecnica

- Captura de Jira con la subtarea KAN-14 finalizada.
- DoD visible en Jira: el servicio arranca en local de forma aislada y limpia.

## Definition of Done

- `shipping-service` puede ejecutarse de forma independiente.
- El puerto local queda definido sin conflictos.
- La base del microservicio queda preparada para consumidor Kafka, persistencia y eventos.