# KAN-10 - Adaptador de Persistencia JPA y API REST con Swagger

## Objetivo

Exponer la funcionalidad principal de pedidos por HTTP y persistir los datos con JPA sobre PostgreSQL.

## Alcance implementado

1. Se implementa el adaptador de persistencia JPA para pedidos.
2. Se crea el controlador REST para operaciones de creacion y consulta.
3. Se documenta la API con Swagger/OpenAPI.
4. Se valida el flujo completo con pruebas sobre el adaptador.

## Evidencia tecnica

- Captura de Jira con la subtarea KAN-10 finalizada.
- DoD visible en Jira: endpoint funcional y persistencia real en base de datos SQL.

## Definition of Done

- La API REST publica las operaciones necesarias del dominio.
- Las entidades se guardan y recuperan correctamente con JPA.
- Swagger deja la superficie HTTP documentada para consumo.