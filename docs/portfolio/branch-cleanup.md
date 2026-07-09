# Limpieza de ramas (post-integracion)

## Objetivo

Mantener un historial limpio y profesional despues de integrar cambios en `main`.

## Flujo recomendado

1. Actualizar remoto:

```bash
git fetch --all --prune
```

2. Identificar ramas ya mergeadas:

```bash
git branch --merged main
```

3. Eliminar ramas locales de feature cerradas:

```bash
git branch -d feature/kan-38
git branch -d feature/kan-39
git branch -d feature/kan-40
git branch -d feature/kan-41
```

4. Eliminar ramas remotas cerradas:

```bash
git push origin --delete feature/kan-38
git push origin --delete feature/kan-39
git push origin --delete feature/kan-40
git push origin --delete feature/kan-41
```

## Nota

Usar `-D` solo si una rama no esta mergeada y se decide eliminar de forma forzada.
