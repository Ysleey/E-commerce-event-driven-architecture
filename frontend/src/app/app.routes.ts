import { Routes } from '@angular/router';
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
			{ path: 'catalogo', component: CatalogPageComponent },
			{ path: 'carrito', component: CartPageComponent },
			{ path: 'checkout', component: CheckoutPageComponent },
			{ path: 'seguimiento', component: TrackingPageComponent },
			{ path: 'login', component: LoginPageComponent },
		],
	},
	{ path: '**', redirectTo: '' },
];
