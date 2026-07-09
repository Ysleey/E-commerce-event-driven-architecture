import { Injectable, computed, signal } from '@angular/core';
import { AuthSession } from '../models/auth.models';

const SESSION_STORAGE_KEY = 'ecommerce.auth.session';
export type AuthSessionEvent = 'none' | 'expired' | 'unauthorized' | 'manual';

@Injectable({ providedIn: 'root' })
export class AuthSessionService {
  private readonly lastEventSignal = signal<AuthSessionEvent>('none');
  private readonly sessionSignal = signal<AuthSession | null>(this.readSession());

  readonly session = computed(() => this.getValidSession());
  readonly token = computed(() => this.getValidSession()?.token ?? null);
  readonly username = computed(() => this.getValidSession()?.username ?? null);
  readonly roles = computed(() => this.getValidSession()?.roles ?? []);
  readonly isAuthenticated = computed(() => this.getValidSession() !== null);

  setSession(session: AuthSession): void {
    this.sessionSignal.set(session);
    this.lastEventSignal.set('none');
    localStorage.setItem(SESSION_STORAGE_KEY, JSON.stringify(session));
  }

  clear(reason: AuthSessionEvent = 'manual'): void {
    this.sessionSignal.set(null);
    this.lastEventSignal.set(reason);
    localStorage.removeItem(SESSION_STORAGE_KEY);
  }

  consumeLastEvent(): AuthSessionEvent {
    const event = this.lastEventSignal();
    this.lastEventSignal.set('none');
    return event;
  }

  private getValidSession(): AuthSession | null {
    const session = this.sessionSignal();
    if (!session) {
      return null;
    }

    if (session.expiresAt <= Date.now()) {
      this.clear('expired');
      return null;
    }

    return session;
  }

  private readSession(): AuthSession | null {
    const raw = localStorage.getItem(SESSION_STORAGE_KEY);
    if (!raw) {
      return null;
    }

    try {
      const parsed = JSON.parse(raw) as AuthSession;
      if (!parsed.expiresAt || parsed.expiresAt <= Date.now()) {
        this.lastEventSignal.set('expired');
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
