# KAN-6 - Estructura Base Multi-modulo de order-service

## Objetivo

Crear la base del microservicio de pedidos con una estructura clara, escalable y alineada con arquitectura hexagonal.

## Alcance implementado

1. Se crea el proyecto Spring Boot del microservicio `order-service`.
2. Se organiza la estructura de paquetes segun hexagonal: `domain`, `application` y `infrastructure`.
3. Se configura el `pom.xml` con dependencias basicas de Web, JPA y Lombok.
4. Se deja el proyecto listo para compilar correctamente con Maven.

## Evidencia tecnica

- Captura de Jira con la subtarea KAN-6 en estado finalizada.
- Descripcion visible en Jira con el criterio de compilacion correcta como DoD.

## Definition of Done

- `order-service` compila sin errores de Maven.
- La estructura base permite continuar con controladores, casos de uso, persistencia y eventos.
- El proyecto queda preparado para las siguientes KAN del backend.