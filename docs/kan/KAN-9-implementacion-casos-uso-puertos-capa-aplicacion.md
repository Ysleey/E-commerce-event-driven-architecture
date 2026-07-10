# KAN-9 - Implementacion de Casos de Uso y Puertos (Capa de Aplicacion)

## Objetivo

Definir la capa de aplicacion para orquestar el comportamiento del dominio mediante puertos de entrada y salida.

## Alcance implementado

1. Se define la interfaz de puerto de salida para acceso a persistencia.
2. Se implementa la clase de servicio de aplicacion para crear pedidos y coordinar dependencias.
3. Se cubren los casos de uso con pruebas unitarias usando mocks.
4. Se mantiene la separacion entre logica de aplicacion y detalles de infraestructura.

## Evidencia tecnica

- Captura de Jira con la subtarea KAN-9 finalizada.
- Descripcion visible en Jira con foco en puertos y capa de aplicacion.

## Definition of Done

- Los casos de uso quedan desacoplados de persistencia y web.
- Los puertos permiten evolucionar la infraestructura sin romper el negocio.
- Los tests unitarios validan la orchestration de la capa de aplicacion.