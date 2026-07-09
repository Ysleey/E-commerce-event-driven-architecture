import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AuthService } from '../auth/services/auth.service';
import { AuthSessionService } from '../auth/services/auth-session.service';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { CartService } from '../../features/cart/services/cart.service';

interface NavItem {
  label: string;
  path: string;
}

@Component({
  selector: 'app-shell',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, RouterOutlet],
  templateUrl: './app-shell.component.html',
})
export class AppShellComponent {
  constructor(
    private readonly authService: AuthService,
    private readonly router: Router,
    readonly authSession: AuthSessionService,
    readonly cart: CartService,
  ) {}

  readonly navItems: NavItem[] = [
    { label: 'Inicio', path: '/' },
    { label: 'Catalogo', path: '/catalogo' },
    { label: 'Carrito', path: '/carrito' },
    { label: 'Checkout', path: '/checkout' },
    { label: 'Seguimiento', path: '/seguimiento' },
    { label: 'Login', path: '/login' },
  ];

  logout(): void {
    this.authService.logout();
    void this.router.navigate(['/login'], {
      queryParams: { reason: 'logout' },
    });
  }
}
