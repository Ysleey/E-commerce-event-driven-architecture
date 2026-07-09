import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthSessionService } from '../../auth/services/auth-session.service';

export const authErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const authSession = inject(AuthSessionService);
  const router = inject(Router);

  return next(req).pipe(
    catchError((error: unknown) => {
      if (error instanceof HttpErrorResponse && (error.status === 401 || error.status === 403) && authSession.isAuthenticated()) {
        const returnUrl = router.url || '/';
        const reason = error.status === 403 ? 'forbidden' : 'unauthorized';
        authSession.clear('unauthorized');
        void router.navigate(['/login'], {
          queryParams: {
            reason,
            returnUrl,
          },
        });
      }

      return throwError(() => error);
    }),
  );
};
