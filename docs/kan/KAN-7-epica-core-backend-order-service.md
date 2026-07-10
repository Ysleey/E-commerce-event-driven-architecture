# KAN-7 - Epica 2: Core Backend - order-service

## Objetivo

Desarrollar el microservicio principal de gestion de pedidos con Spring Boot 3.x, Java 21 y Maven, cubriendo dominio, aplicacion, persistencia, seguridad y publicacion de eventos.

## Alcance implementado

1. Se modela el dominio de pedidos con entidades y reglas de negocio puras.
2. Se implementan los casos de uso y los puertos de aplicacion.
3. Se expone una API REST segura con Swagger/OpenAPI.
4. Se asegura el acceso a endpoints con Spring Security y JWT.
5. Se prepara la publicacion de eventos hacia Kafka y la observabilidad base.

## Subtareas visibles

- [KAN-8 - Modelado de entidades de dominio y logica de negocio (TDD)](KAN-8-modelado-entidades-dominio-logica-negocio-tdd.md)
- [KAN-9 - Implementacion de casos de uso y puertos (capa de aplicacion)](KAN-9-implementacion-casos-uso-puertos-capa-aplicacion.md)
- [KAN-10 - Adaptador de persistencia JPA y API REST con Swagger](KAN-10-adaptador-persistencia-jpa-api-rest-swagger.md)
- [KAN-11 - Aseguramiento de endpoints con Spring Security y JWT](KAN-11-aseguramiento-endpoints-spring-security-jwt.md)
- [KAN-12 - Configuracion del productor de eventos Apache Kafka](KAN-12-configuracion-productor-eventos-apache-kafka.md)
- [KAN-22 - Contrato API OpenAPI v1](KAN-22-openapi-v1.md)
- [KAN-23 - Contrato de eventos Kafka v1](KAN-23-kafka-contract-v1.md)
- [KAN-24 - Observabilidad minima](KAN-24-observabilidad-minima.md)
- [KAN-25 - Resiliencia retry y DLQ](KAN-25-resiliencia-retry-dlq.md)

## Evidencia tecnica

La captura de Jira muestra la epica 2 con las subtareas KAN-8 a KAN-12 como bloque base del backend, junto con los entregables funcionales ya documentados en KAN-22 a KAN-25.

## Definition of Done

- El dominio de pedidos queda modelado y probado.
- La capa de aplicacion queda desacoplada por puertos y casos de uso.
- La API REST queda documentada y segura.
- El backend queda preparado para persistencia, mensajeria y observabilidad.