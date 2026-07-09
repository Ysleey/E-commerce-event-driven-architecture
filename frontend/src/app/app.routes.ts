import { Routes } from '@angular/router';
import { authGuard } from './core/auth/guards/auth.guard';
import { AppShellComponent } from './core/layout/app-shell.component';
import { LoginPageComponent } from './features/auth/pages/login-page.component';
import { CartPageComponent } from './features/cart/pages/cart-page.component';
import { CatalogPageComponent } from './features/catalog/pages/catalog-page.component';
import { CheckoutPageComponent } from './features/checkout/pages/checkout-page.component';
import { HomePageComponent } from './features/home/pages/home-page.component';
import { TrackingPageComponent } from './features/tracking/pages/tracking-page.component';

export const routes: Routes = [
	{
		path: '',
		component: AppShellComponent,
		children: [
			{ path: '', component: HomePageComponent, pathMatch: 'full' },
			{ path: 'catalogo', component: CatalogPageComponent, canActivate: [authGuard] },
			{ path: 'carrito', component: CartPageComponent, canActivate: [authGuard] },
			{ path: 'checkout', component: CheckoutPageComponent, canActivate: [authGuard] },
			{ path: 'seguimiento', component: TrackingPageComponent, canActivate: [authGuard] },
			{ path: 'login', component: LoginPageComponent },
		],
	},
	{ path: '**', redirectTo: '' },
];
