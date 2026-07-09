import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { AuthSession, LoginRequest, LoginResponse } from '../models/auth.models';
import { ApiClientService } from '../../http/services/api-client.service';
import { AuthSessionService } from './auth-session.service';

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(
    private readonly apiClient: ApiClientService,
    private readonly sessionService: AuthSessionService,
  ) {}

  login(payload: LoginRequest): Observable<LoginResponse> {
    return this.apiClient
      .post<LoginRequest, LoginResponse>('/api/v1/auth/login', payload)
      .pipe(
        tap((response) => {
          const session: AuthSession = {
            token: response.token,
            username: response.username,
            roles: response.roles,
            expiresAt: Date.now() + response.expiresInSeconds * 1000,
          };
          this.sessionService.setSession(session);
        }),
      );
  }

  logout(): void {
    this.sessionService.clear('manual');
  }
}
