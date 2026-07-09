import { Component } from '@angular/core';
import { PagePlaceholderComponent } from '../../../shared/ui/page-placeholder/page-placeholder.component';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [PagePlaceholderComponent],
  template: `
    <app-page-placeholder
      title="Login y autenticacion"
      description="Aqui implementaremos el flujo de login JWT, almacenamiento seguro de sesion, guard y redireccion de rutas protegidas."
    />
  `,
})
export class LoginPageComponent {}
