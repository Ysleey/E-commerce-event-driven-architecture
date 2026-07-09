import { Injectable, computed, signal } from '@angular/core';
import { AuthSession } from '../models/auth.models';

const SESSION_STORAGE_KEY = 'ecommerce.auth.session';

@Injectable({ providedIn: 'root' })
export class AuthSessionService {
  private readonly sessionSignal = signal<AuthSession | null>(this.readSession());

  readonly session = computed(() => this.sessionSignal());
  readonly token = computed(() => this.sessionSignal()?.token ?? null);
  readonly username = computed(() => this.sessionSignal()?.username ?? null);
  readonly roles = computed(() => this.sessionSignal()?.roles ?? []);
  readonly isAuthenticated = computed(() => {
    const session = this.sessionSignal();
    if (!session) {
      return false;
    }
    return session.expiresAt > Date.now();
  });

  setSession(session: AuthSession): void {
    this.sessionSignal.set(session);
    localStorage.setItem(SESSION_STORAGE_KEY, JSON.stringify(session));
  }

  clear(): void {
    this.sessionSignal.set(null);
    localStorage.removeItem(SESSION_STORAGE_KEY);
  }

  private readSession(): AuthSession | null {
    const raw = localStorage.getItem(SESSION_STORAGE_KEY);
    if (!raw) {
      return null;
    }

    try {
      const parsed = JSON.parse(raw) as AuthSession;
      if (!parsed.expiresAt || parsed.expiresAt <= Date.now()) {
        localStorage.removeItem(SESSION_STORAGE_KEY);
        return null;
      }
      return parsed;
    } catch {
      localStorage.removeItem(SESSION_STORAGE_KEY);
      return null;
    }
  }
}
