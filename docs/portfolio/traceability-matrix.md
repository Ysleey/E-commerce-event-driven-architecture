# Matriz de trazabilidad Jira a GitHub

## Objetivo

Consolidar la trazabilidad entre tareas Jira y evidencia tecnica en repositorio para evaluacion de portafolio.

## Convencion de commits

Formato recomendado:

- `feat(scope): KAN-XX descripcion breve`
- `fix(scope): KAN-XX descripcion breve`
- `docs(scope): KAN-XX descripcion breve`
- `test(scope): KAN-XX descripcion breve`

Ejemplo:
- `feat(order-service): KAN-40 observabilidad con correlationId y actuators`

## Plantilla de vinculacion por KAN

Completar al cerrar cada KAN:

| KAN | Evidencia tecnica | Commit(s) | PR |
|-----|-------------------|-----------|----|
| KAN-38 | docs/kan/KAN-38-configuracion-integracion-frontend-backend.md | `<hash-1>` | `<url-pr>` |
| KAN-39 | docs/kan/KAN-39-calidad-tecnica-frontend.md | `<hash-2>` | `<url-pr>` |
| KAN-40 | docs/kan/KAN-40-ci-minima-frontend.md | `<hash-3>` | `<url-pr>` |
| KAN-41 | docs/kan/KAN-41-evidencia-cierre-portafolio-epica.md | `<hash-4>` | `<url-pr>` |

## Comandos utiles

Listar commits por ticket:

```bash
git log --oneline --grep "KAN-40"
```

Ver cambios de un commit:

```bash
git show <hash>
```

## Criterio de cierre

Una KAN se considera cerrada cuando:

- existe documento de evidencia en `docs/kan`,
- hay al menos un commit con referencia al ticket,
- existe PR asociado (si aplica flujo con ramas),
- los comandos de validacion (build/test/lint) estan en verde.
