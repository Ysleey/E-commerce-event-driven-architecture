# KAN-8 - Modelado de Entidades de Dominio y Logica de Negocio (TDD)

## Objetivo

Crear las clases de dominio inmutables del núcleo de pedidos y validar su comportamiento mediante TDD.

## Alcance implementado

1. Se crean entidades y value objects del dominio, como `Order`, `OrderItem` y `Product`.
2. Se aplican reglas de negocio puras en el paquete `domain`.
3. Se escriben tests unitarios antes o junto a la implementacion para validar calculos y restricciones.
4. Se comprueba la cobertura del modelo de dominio con pruebas automatizadas.

## Evidencia tecnica

- Captura de Jira con la subtarea KAN-8 finalizada.
- Descripcion visible en Jira con enfoque TDD para el modelado del dominio.

## Definition of Done

- El modelo de dominio representa correctamente el negocio de pedidos.
- Las reglas de negocio quedan cubiertas por tests verdes.
- El dominio no depende de infraestructura ni frameworks externos.