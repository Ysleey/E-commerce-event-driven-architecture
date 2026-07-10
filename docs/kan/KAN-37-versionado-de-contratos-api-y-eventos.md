# KAN-37 - Versionado de contratos (API y eventos)

## Objetivo

Definir y aplicar una estrategia de versionado para contratos REST y Kafka, con reglas de compatibilidad y gobernanza de cambios.

## Alcance implementado

1. Se establece una politica de versionado para la API REST.
2. Se establece una politica de versionado para eventos Kafka.
3. Se definen reglas de breaking vs non-breaking changes.
4. Se documenta el flujo de aprobacion de cambios de contrato.

## Evidencia tecnica

- Captura de Jira con la subtarea KAN-37 en estado de trabajo.
- DoD visible en Jira: politica de versionado publicada y contratos actuales etiquetados con version explicita.

## Definition of Done

- La version de los contratos queda explicitada y gobernada.
- Existe una guia de migracion para consumidores y productores.
- El cambio de contrato pasa a formar parte del proceso de CI/PR.