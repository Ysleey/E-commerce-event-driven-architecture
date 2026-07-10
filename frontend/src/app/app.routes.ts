import { Routes } from '@angular/router';
import { authGuard } from './core/auth/guards/auth.guard';
import { AppShellComponent } from './core/layout/app-shell.component';

export const routes: Routes = [
	{
		path: '',
		component: AppShellComponent,
		children: [
			{
				path: '',
				loadComponent: () => import('./features/home/pages/home-page.component').then((m) => m.HomePageComponent),
				pathMatch: 'full',
			},
			{
				path: 'stack-tecnologico',
				loadComponent: () => import('./features/stack/pages/stack-page.component').then((m) => m.StackPageComponent),
				canActivate: [authGuard],
			},
			{
				path: 'catalogo',
				loadComponent: () => import('./features/catalog/pages/catalog-page.component').then((m) => m.CatalogPageComponent),
				canActivate: [authGuard],
			},
			{
				path: 'carrito',
				loadComponent: () => import('./features/cart/pages/cart-page.component').then((m) => m.CartPageComponent),
				canActivate: [authGuard],
			},
			{
				path: 'checkout',
				loadComponent: () => import('./features/checkout/pages/checkout-page.component').then((m) => m.CheckoutPageComponent),
				canActivate: [authGuard],
			},
			{
				path: 'seguimiento',
				loadComponent: () => import('./features/tracking/pages/tracking-page.component').then((m) => m.TrackingPageComponent),
				canActivate: [authGuard],
			},
			{
				path: 'mis-pedidos',
				loadComponent: () => import('./features/orders/pages/orders-history-page.component').then((m) => m.OrdersHistoryPageComponent),
				canActivate: [authGuard],
			},
			{
				path: 'login',
				loadComponent: () => import('./features/auth/pages/login-page.component').then((m) => m.LoginPageComponent),
			},
		],
	},
	{ path: '**', redirectTo: '' },
];
