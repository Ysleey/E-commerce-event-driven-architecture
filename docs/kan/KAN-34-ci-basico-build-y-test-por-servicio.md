# KAN-34 - CI basico de build y test por servicio

## Objetivo

Configurar una pipeline de integracion continua inicial para ejecutar build y test de cada servicio y del frontend en cada pull request y en la rama principal.

## Alcance implementado

1. Se define un flujo CI que ejecuta build y test por servicio.
2. Se bloquea el merge si falla algun test.
3. Se expone de forma visible el estado de ejecucion.
4. Se documenta el proceso para el equipo.

## Evidencia tecnica

- Captura de Jira con la subtarea KAN-34 finalizada.
- DoD visible en Jira: pipeline operativa en repositorio y ejemplo en verde.

## Definition of Done

- La pipeline corre build y test por servicio.
- Un fallo de test bloquea el merge segun la politica definida.
- La documentacion de CI queda publicada en el repositorio.