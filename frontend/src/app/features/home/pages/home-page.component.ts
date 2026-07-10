import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Router } from '@angular/router';
import { MOCK_PRODUCTS } from '../../catalog/data/mock-products';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './home-page.component.html',
})
export class HomePageComponent implements OnInit {
  searchQuery = '';
  isLoaded = false;

  readonly quickSearchTerms = ['auriculares', 'raton', 'lampara', 'base escritorio'];

  readonly featuredProducts = [
    this.toFeatured('Auriculares Ember Air', 'Mas vendido', '4.8'),
    this.toFeatured('Auriculares Pulse ANC', 'Mas vendido', '4.7'),
    this.toFeatured('Raton Pro Silent', 'Top', '4.8'),
    this.toFeatured('Raton Arc Wireless', 'Top', '4.7'),
    this.toFeatured('Lampara Smart Warm Desk', 'Nuevo', '4.6'),
    this.toFeatured('Lampara Aura Pro', 'Top', '4.8'),
    this.toFeatured('Base Ember Dual Dock', 'Oferta', '4.6'),
    this.toFeatured('Base Station Hub 7-en-1', 'Mas vendido', '4.7'),
    this.toFeatured('Auriculares Nova Beats', 'Top', '4.5'),
    this.toFeatured('Raton Carbon Office', 'Pro', '4.8'),
    this.toFeatured('Lampara Edge Minimal', 'Home', '4.6'),
    this.toFeatured('Base Desk Magnetic Duo', 'Bundle', '4.7'),
    this.toFeatured('Auriculares Orbit Bass', 'Oferta', '4.7'),
    this.toFeatured('Raton Ember Click', 'Top', '4.5'),
    this.toFeatured('Lampara Orbit Focus', 'Nuevo', '4.8'),
    this.toFeatured('Base Work Dock Ultra', 'Pro', '4.9'),
  ];

  private toFeatured(name: string, tag: string, rating: string) {
    const product = MOCK_PRODUCTS.find((item) => item.name === name);

    return {
      tag,
      name,
      price: `$${(product?.price ?? 0).toFixed(2)}`,
      rating,
      image: product?.imageUrl ?? 'https://loremflickr.com/900/900/technology?lock=99999',
    };
  }

  constructor(private readonly router: Router) {}

  ngOnInit(): void {
    requestAnimationFrame(() => {
      this.isLoaded = true;
    });
  }

  executeSearch(term?: string): void {
    const query = (term ?? this.searchQuery).trim();
    this.router.navigate(['/catalogo'], {
      queryParams: query.length > 0 ? { q: query } : {},
    });
  }

  applyQuickSearch(term: string): void {
    this.searchQuery = term;
    this.executeSearch(term);
  }

}
