import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

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
  readonly navItems: NavItem[] = [
    { label: 'Inicio', path: '/' },
    { label: 'Catalogo', path: '/catalogo' },
    { label: 'Carrito', path: '/carrito' },
    { label: 'Checkout', path: '/checkout' },
    { label: 'Seguimiento', path: '/seguimiento' },
    { label: 'Login', path: '/login' },
  ];
}
