import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
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

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router,
  ) {}

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
          void this.router.navigateByUrl('/catalogo');
        },
        error: (error: ApiError) => {
          this.errorMessage = error.message;
        },
      });
  }
}
