# KAN-5 - Configuracion del Entorno de Red Local con Docker Compose

## Objetivo

Preparar el entorno local minimo para levantar los servicios de infraestructura que necesita el proyecto durante el desarrollo.

## Alcance implementado

1. Se crea el archivo `docker-compose.yml` en la raiz del proyecto.
2. Se levanta una base de datos PostgreSQL para el dominio de pedidos.
3. Se levanta una instancia local de Apache Kafka con Zookeeper o KRaft.
4. Se valida la disponibilidad de los servicios en sus puertos correspondientes.

## Evidencia tecnica

- Captura de Jira con la subtarea KAN-5 en estado finalizada.
- DoD visible en la tarjeta: ejecutar `docker-compose up -d` y verificar PostgreSQL y Kafka.

## Definition of Done

- `docker-compose.yml` existe y describe la infraestructura local.
- PostgreSQL y Kafka pueden iniciarse para el desarrollo local.
- El proyecto queda preparado para soportar el backend y los flujos de integracion posteriores.