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
      if (error instanceof HttpErrorResponse && error.status === 401 && authSession.isAuthenticated()) {
        const returnUrl = router.url || '/';
        authSession.clear('unauthorized');
        void router.navigate(['/login'], {
          queryParams: {
            reason: 'unauthorized',
            returnUrl,
          },
        });
      }

      return throwError(() => error);
    }),
  );
};
