import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { EmptyStateCardComponent } from '../../../shared/ui/empty-state-card/empty-state-card.component';
import { FeedbackAlertComponent } from '../../../shared/ui/feedback-alert/feedback-alert.component';
import { CartService } from '../../cart/services/cart.service';
import { MOCK_PRODUCTS } from '../data/mock-products';
import { Product } from '../models/product.model';

@Component({
  selector: 'app-catalog-page',
  standalone: true,
  imports: [CommonModule, FormsModule, EmptyStateCardComponent, FeedbackAlertComponent],
  templateUrl: './catalog-page.component.html',
})
export class CatalogPageComponent {
  readonly products = MOCK_PRODUCTS;
  readonly maxAvailablePrice = Math.ceil(Math.max(...this.products.map((product) => product.price)) / 10) * 10;
  readonly categories = [
    { value: 'all', label: 'Todas' },
    { value: 'portatiles', label: 'Portatiles' },
    { value: 'tablets', label: 'Tablets' },
    { value: 'sobremesas', label: 'Sobremesas' },
    { value: 'monitores', label: 'Monitores' },
    { value: 'componentes', label: 'Componentes' },
    { value: 'accesorios-informatica', label: 'Accesorios de informatica' },
    { value: 'impresoras-tinta', label: 'Impresoras y tinta' },
    { value: 'software', label: 'Software' },
    { value: 'gaming', label: 'Gaming' },
    { value: 'juegos-pc', label: 'Juegos para PC' },
    { value: 'auriculares', label: 'Auriculares' },
    { value: 'raton', label: 'Raton' },
    { value: 'teclado', label: 'Teclado' },
    { value: 'lampara', label: 'Lampara' },
    { value: 'base-escritorio', label: 'Bases de escritorio' },
  ];

  selectedCategory = 'all';
  searchTerm = '';
  maxPrice = this.maxAvailablePrice;
  feedbackMessage = '';

  private normalizeText(value: string): string {
    return value
      .trim()
      .toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '');
  }

  constructor(
    private readonly cartService: CartService,
    private readonly route: ActivatedRoute,
  ) {
    this.route.queryParamMap.subscribe((params) => {
      const q = params.get('q');
      if (q !== null) {
        this.searchTerm = q;
      }
    });
  }

  get filteredProducts(): Product[] {
    const search = this.normalizeText(this.searchTerm);
    return this.products.filter((product) => {
      const categoryMatch = this.selectedCategory === 'all' || product.category === this.selectedCategory;
      const priceMatch = product.price <= this.maxPrice;
      const searchableContent = [product.name, product.description, ...(product.searchTerms ?? [])]
        .map((value) => this.normalizeText(value))
        .join(' ');
      const searchMatch =
        search.length === 0 ||
        searchableContent.includes(search);

      return categoryMatch && priceMatch && searchMatch;
    });
  }

  getCategoryCount(category: string): number {
    if (category === 'all') {
      return this.products.length;
    }

    return this.products.filter((product) => product.category === category).length;
  }

  selectCategory(category: string): void {
    this.selectedCategory = category;
    this.searchTerm = '';
    this.maxPrice = this.maxAvailablePrice;
  }

  onCategoryChange(): void {
    this.selectCategory(this.selectedCategory);
  }

  addToCart(product: Product): void {
    this.cartService.addProduct(product);
    this.feedbackMessage = `${product.name} anadido al carrito.`;
  }
}
