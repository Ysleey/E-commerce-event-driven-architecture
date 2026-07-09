# KAN-21 - Pruebas unitarias de componentes y documentacion de portafolio

## Objetivo
Cerrar la subtarea de calidad del frontend garantizando:
- cobertura de pruebas unitarias para logica clave (carrito y capa HTTP)
- documentacion README de nivel portafolio profesional en la raiz del repositorio

## Implementacion realizada

### 1) Pruebas unitarias frontend
Se agregaron pruebas para validar comportamiento funcional de compra y resiliencia de integracion HTTP.

#### A. Carrito de compras
Archivo:
- `frontend/src/app/features/cart/services/cart.service.spec.ts`

Cobertura funcional:
- alta de productos y calculo de subtotal/impuestos/envio/total
- tope por stock al incrementar unidades
- actualizacion de cantidad
- eliminacion por cantidad cero
- vaciado de carrito

#### B. Manejo de respuestas HTTP
Archivos:
- `frontend/src/app/core/http/services/http-error-mapper.service.spec.ts`
- `frontend/src/app/core/http/services/api-client.service.spec.ts`

Cobertura funcional:
- mapeo de errores de red y not-found
- mapeo de error desconocido
- GET exitoso con respuesta backend
- propagacion de ApiError ante 404
- timeout configurable en cliente HTTP

### 2) README corporativo en raiz
Archivo actualizado:
- `README.md`

Mejoras aplicadas:
- resumen ejecutivo de solucion
- diagramas de arquitectura y capas hexagonales (Mermaid)
- alcance backend + frontend por entregables
- estrategia de calidad (build/tests backend y frontend)
- guia de ejecucion local ampliada (incluye frontend y workaround Windows)
- convenciones de flujo de trabajo y commits orientados a resultado

## Validacion tecnica

### Frontend tests
Comando ejecutado:
- `cmd /c npm run test -- --watch=false --browsers=ChromeHeadless --progress=false`

Resultado obtenido:
- `TOTAL: 14 SUCCESS`

### Frontend build
Comando ejecutado:
- `cmd /c npm run build`

Resultado obtenido:
- `Application bundle generation complete.`

## Criterios de aceptacion cubiertos
- Se amplian las pruebas unitarias del frontend sobre piezas criticas de negocio.
- Se documenta el proyecto con narrativa y estructura apta para portafolio profesional.
- El repositorio queda preparado para presentacion tecnica y publicacion en LinkedIn.
