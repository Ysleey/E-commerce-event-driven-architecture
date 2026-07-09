import { HttpClient, provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed, fakeAsync, tick } from '@angular/core/testing';
import { API_BASE_URL } from '../../config/api.config';
import { ApiError } from '../models/api-error.model';
import { ApiClientService } from './api-client.service';
import { HttpErrorMapperService } from './http-error-mapper.service';

describe('ApiClientService', () => {
  let service: ApiClientService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting(), HttpClient, ApiClientService, HttpErrorMapperService],
    });

    service = TestBed.inject(ApiClientService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('ejecuta GET y devuelve respuesta del backend', () => {
    let response: { ok: boolean } | undefined;

    service.get<{ ok: boolean }>('/health').subscribe((result) => {
      response = result;
    });

    const req = httpMock.expectOne(`${API_BASE_URL}/health`);
    expect(req.request.method).toBe('GET');
    req.flush({ ok: true });

    expect(response).toEqual({ ok: true });
  });

  it('mapea 404 a ApiError not-found', () => {
    let capturedError: ApiError | undefined;

    service.get('/api/orders/by-order-number/NOT-EXISTS', { retryCount: 0 }).subscribe({
      next: () => fail('No deberia emitir next en 404'),
      error: (error: ApiError) => {
        capturedError = error;
      },
    });

    const req = httpMock.expectOne(`${API_BASE_URL}/api/orders/by-order-number/NOT-EXISTS`);
    req.flush({}, { status: 404, statusText: 'Not Found' });

    expect(capturedError).toEqual(
      jasmine.objectContaining({
        kind: 'not-found',
        status: 404,
      }),
    );
  });

  it('respeta timeout configurado', fakeAsync(() => {
    let capturedError: ApiError | undefined;

    service.get('/delayed', { timeoutMs: 5, retryCount: 0 }).subscribe({
      next: () => fail('No deberia emitir next en timeout'),
      error: (error: ApiError) => {
        capturedError = error;
      },
    });

    httpMock.expectOne(`${API_BASE_URL}/delayed`);
    tick(6);

    expect(capturedError).toEqual(
      jasmine.objectContaining({
        kind: 'unknown',
      }),
    );
  }));
});
