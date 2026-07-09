import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, retry, timeout } from 'rxjs/operators';
import { API_BASE_URL } from '../../config/api.config';
import { ApiError } from '../models/api-error.model';
import { HttpErrorMapperService } from './http-error-mapper.service';

export interface RequestOptions {
  timeoutMs?: number;
  retryCount?: number;
  context?: HttpContext;
}

@Injectable({ providedIn: 'root' })
export class ApiClientService {
  constructor(
    private readonly http: HttpClient,
    private readonly errorMapper: HttpErrorMapperService,
  ) {}

  get<T>(path: string, options?: RequestOptions): Observable<T> {
    return this.http
      .get<T>(this.url(path), { context: options?.context })
      .pipe(
        timeout(options?.timeoutMs ?? 10_000),
        retry({ count: options?.retryCount ?? 1 }),
        catchError((error) => this.handleError(error)),
      );
  }

  post<TRequest, TResponse>(path: string, payload: TRequest, options?: RequestOptions): Observable<TResponse> {
    return this.http
      .post<TResponse>(this.url(path), payload, { context: options?.context })
      .pipe(
        timeout(options?.timeoutMs ?? 10_000),
        catchError((error) => this.handleError(error)),
      );
  }

  patch<TRequest, TResponse>(path: string, payload: TRequest, options?: RequestOptions): Observable<TResponse> {
    return this.http
      .patch<TResponse>(this.url(path), payload, { context: options?.context })
      .pipe(
        timeout(options?.timeoutMs ?? 10_000),
        catchError((error) => this.handleError(error)),
      );
  }

  private url(path: string): string {
    return `${API_BASE_URL}${path}`;
  }

  private handleError(error: unknown): Observable<never> {
    const mapped: ApiError = this.errorMapper.map(error);
    return throwError(() => mapped);
  }
}
