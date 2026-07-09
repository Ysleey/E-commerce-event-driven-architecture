import { HttpErrorResponse } from '@angular/common/http';
import { HttpErrorMapperService } from './http-error-mapper.service';

describe('HttpErrorMapperService', () => {
  let service: HttpErrorMapperService;

  beforeEach(() => {
    service = new HttpErrorMapperService();
  });

  it('mapea error de red con mensaje de soporte', () => {
    const mapped = service.map(new HttpErrorResponse({ status: 0 }));

    expect(mapped.kind).toBe('network');
    expect(mapped.status).toBe(0);
    expect(mapped.details).toContain('localhost:8080');
  });

  it('mapea 404 como not-found', () => {
    const mapped = service.map(new HttpErrorResponse({ status: 404 }));

    expect(mapped.kind).toBe('not-found');
    expect(mapped.message).toContain('Recurso no encontrado');
  });

  it('mapea error desconocido para objetos no HttpErrorResponse', () => {
    const mapped = service.map(new Error('boom'));

    expect(mapped.kind).toBe('unknown');
    expect(mapped.status).toBe(0);
  });
});
