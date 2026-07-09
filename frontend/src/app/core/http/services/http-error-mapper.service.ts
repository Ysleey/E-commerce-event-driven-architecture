import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApiError } from '../models/api-error.model';

@Injectable({ providedIn: 'root' })
export class HttpErrorMapperService {
  map(error: unknown): ApiError {
    if (!(error instanceof HttpErrorResponse)) {
      return {
        kind: 'unknown',
        status: 0,
        message: 'Error inesperado en el cliente.',
      };
    }

    if (error.status === 0) {
      return {
        kind: 'network',
        status: 0,
        message: 'No se pudo conectar con el backend.',
        details: 'Verifica que order-service este levantado en localhost:8080.',
      };
    }

    if (error.status === 400) {
      return {
        kind: 'validation',
        status: 400,
        message: 'Solicitud invalida. Revisa los datos enviados.',
      };
    }

    if (error.status === 401) {
      return {
        kind: 'auth',
        status: 401,
        message: 'Tu sesion no es valida o ha expirado.',
      };
    }

    if (error.status === 403) {
      return {
        kind: 'forbidden',
        status: 403,
        message: 'No tienes permisos para esta accion.',
      };
    }

    if (error.status === 404) {
      return {
        kind: 'not-found',
        status: 404,
        message: 'Recurso no encontrado.',
      };
    }

    if (error.status >= 500) {
      return {
        kind: 'server',
        status: error.status,
        message: 'El servidor no pudo procesar la solicitud.',
      };
    }

    return {
      kind: 'unknown',
      status: error.status,
      message: 'Se produjo un error no controlado.',
    };
  }
}
