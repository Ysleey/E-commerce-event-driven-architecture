# Checklist de cierre final del proyecto

## Estado actual

- Smoke tests de Postman en verde (autenticacion, ciclo de vida de ordenes y health checks).
- Indice KAN consolidado en `docs/kan/README.md` con epicas y subtareas.
- Documentacion de arquitectura, contratos y calidad disponible.

## Cierre minimo para declarar proyecto terminado

### 1) Cerrar trazabilidad Jira a GitHub

Completar en `docs/portfolio/traceability-matrix.md`:

- hashes reales de commits por KAN,
- URL reales de PR,
- validacion de que cada KAN tenga evidencia tecnica enlazada.

Salida esperada:

- matriz sin placeholders,
- evidencia auditable end-to-end por ticket.

### 2) Publicar evidencia visual final

Generar y guardar un GIF o video corto en `docs/portfolio/assets/` siguiendo `docs/portfolio/demo-checklist.md`.

Salida esperada:

- archivo de demo publicado en el repositorio,
- nota en Jira con enlace directo a la evidencia.

### 3) Congelar version de entrega

Ejecutar validacion final en local y CI:

- `order-service`: build + test,
- `shipping-service`: build + test,
- `frontend`: lint + test + build,
- Postman smoke run en verde.

Luego:

- actualizar changelog de cierre,
- crear tag de version final (por ejemplo `v1.0.0`),
- dejar release notes con alcance y evidencia.

Salida esperada:

- release final reproducible,
- narrativa de cierre lista para evaluacion tecnica.

## Cierre recomendado (extra profesional)

- Adjuntar captura de pipeline CI en verde en docs/portfolio.
- Añadir tabla de riesgos pendientes y mejoras futuras en roadmap.
- Registrar decision de versionado final de contratos si hubo cambios de ultima hora.