# KAN-11 - Aseguramiento de Endpoints con Spring Security y JWT

## Objetivo

Proteger los endpoints del backend con autenticacion JWT y una cadena de filtros consistente.

## Alcance implementado

1. Se configura Spring Security para separar endpoints publicos y protegidos.
2. Se valida el token JWT en cada peticion HTTP a la API de pedidos.
3. Se rechazan solicitudes sin token o con token invalido.
4. Se documenta el comportamiento esperado para integracion con frontend y Postman.

## Evidencia tecnica

- Captura de Jira con la subtarea KAN-11 finalizada.
- DoD visible en Jira: las peticiones sin token retornan `401 Unauthorized`.

## Definition of Done

- La API distingue correctamente entre acceso publico y protegido.
- Los endpoints protegidos no aceptan peticiones anonimas.
- El flujo JWT queda listo para integrarse con el frontend.