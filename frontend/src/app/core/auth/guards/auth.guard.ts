import { inject } from '@angular/core';
import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { AuthSessionService } from '../services/auth-session.service';

export const authGuard: CanActivateFn = (_route, state): boolean | UrlTree => {
  const authSession = inject(AuthSessionService);
  const router = inject(Router);

  if (authSession.isAuthenticated()) {
    return true;
  }

  const event = authSession.consumeLastEvent();
  const reason = event === 'expired' ? 'expired' : 'required';

  return router.createUrlTree(['/login'], {
    queryParams: {
      reason,
      returnUrl: state.url,
    },
  });
};
