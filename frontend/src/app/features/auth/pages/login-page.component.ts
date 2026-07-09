import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { finalize } from 'rxjs/operators';
import { ApiError } from '../../../core/http/models/api-error.model';
import { AuthService } from '../../../core/auth/services/auth.service';
import { isBlank } from '../../../shared/utils/input-validators';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login-page.component.html',
})
export class LoginPageComponent {
  username = 'admin';
  password = 'admin123';
  isSubmitting = false;
  validationMessage = '';
  errorMessage = '';
  infoMessage = '';
  private returnUrl = '/catalogo';

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router,
    private readonly route: ActivatedRoute,
  ) {
    this.route.queryParamMap.subscribe((params) => {
      const reason = params.get('reason');
      const returnUrl = params.get('returnUrl');

      if (returnUrl && returnUrl.startsWith('/')) {
        this.returnUrl = returnUrl;
      }

      if (reason === 'expired') {
        this.infoMessage = 'Tu sesion expiro. Inicia sesion de nuevo para continuar.';
      } else if (reason === 'unauthorized') {
        this.infoMessage = 'Tu token ya no es valido. Inicia sesion nuevamente.';
      } else if (reason === 'required') {
        this.infoMessage = 'Necesitas iniciar sesion para acceder a esa pantalla.';
      } else if (reason === 'logout') {
        this.infoMessage = 'Sesion cerrada correctamente.';
      } else {
        this.infoMessage = '';
      }
    });
  }

  submit(): void {
    this.validationMessage = '';
    this.errorMessage = '';

    if (isBlank(this.username) || isBlank(this.password)) {
      this.validationMessage = 'Usuario y password son obligatorios.';
      return;
    }

    this.isSubmitting = true;
    this.authService
      .login({
        username: this.username.trim(),
        password: this.password,
      })
      .pipe(finalize(() => (this.isSubmitting = false)))
      .subscribe({
        next: () => {
          const destination = this.returnUrl.startsWith('/login') ? '/catalogo' : this.returnUrl;
          void this.router.navigateByUrl(destination);
        },
        error: (error: ApiError) => {
          this.errorMessage = error.message;
        },
      });
  }
}
