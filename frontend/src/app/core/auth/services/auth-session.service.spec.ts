import { AuthSession } from '../models/auth.models';
import { AuthSessionService } from './auth-session.service';

describe('AuthSessionService', () => {
  beforeEach(() => {
    localStorage.clear();
  });

  it('should persist and expose valid session', () => {
    const service = new AuthSessionService();
    const session: AuthSession = {
      token: 'token-123',
      username: 'admin',
      roles: ['ADMIN'],
      expiresAt: Date.now() + 60_000,
    };

    service.setSession(session);

    expect(service.isAuthenticated()).toBeTrue();
    expect(service.token()).toBe('token-123');
    expect(service.username()).toBe('admin');
    expect(service.roles()).toEqual(['ADMIN']);
  });

  it('should clear expired session and emit expired event', () => {
    const expiredSession: AuthSession = {
      token: 'expired-token',
      username: 'admin',
      roles: ['ADMIN'],
      expiresAt: Date.now() - 1_000,
    };
    localStorage.setItem('ecommerce.auth.session', JSON.stringify(expiredSession));

    const service = new AuthSessionService();

    expect(service.isAuthenticated()).toBeFalse();
    expect(service.token()).toBeNull();
    expect(localStorage.getItem('ecommerce.auth.session')).toBeNull();
    expect(service.consumeLastEvent()).toBe('expired');
  });

  it('should clear session manually', () => {
    const service = new AuthSessionService();
    const session: AuthSession = {
      token: 'token-123',
      username: 'admin',
      roles: ['ADMIN'],
      expiresAt: Date.now() + 60_000,
    };

    service.setSession(session);
    service.clear('manual');

    expect(service.isAuthenticated()).toBeFalse();
    expect(service.token()).toBeNull();
    expect(service.consumeLastEvent()).toBe('manual');
  });
});
