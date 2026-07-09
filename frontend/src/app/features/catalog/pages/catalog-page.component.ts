import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CartService } from '../../cart/services/cart.service';
import { MOCK_PRODUCTS } from '../data/mock-products';
import { Product } from '../models/product.model';

@Component({
  selector: 'app-catalog-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './catalog-page.component.html',
})
export class CatalogPageComponent {
  readonly products = MOCK_PRODUCTS;
  readonly categories = [
    { value: 'all', label: 'Todas' },
    { value: 'audio', label: 'Audio' },
    { value: 'gaming', label: 'Gaming' },
    { value: 'office', label: 'Oficina' },
    { value: 'lifestyle', label: 'Lifestyle' },
  ];

  selectedCategory = 'all';
  searchTerm = '';
  maxPrice = 250;
  feedbackMessage = '';

  constructor(private readonly cartService: CartService) {}

  get filteredProducts(): Product[] {
    const search = this.searchTerm.trim().toLowerCase();
    return this.products.filter((product) => {
      const categoryMatch = this.selectedCategory === 'all' || product.category === this.selectedCategory;
      const priceMatch = product.price <= this.maxPrice;
      const searchMatch =
        search.length === 0 ||
        product.name.toLowerCase().includes(search) ||
        product.description.toLowerCase().includes(search);

      return categoryMatch && priceMatch && searchMatch;
    });
  }

  addToCart(product: Product): void {
    this.cartService.addProduct(product);
    this.feedbackMessage = `${product.name} anadido al carrito.`;
  }
}
