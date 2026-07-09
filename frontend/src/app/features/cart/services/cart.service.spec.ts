import { CartService } from './cart.service';
import { Product } from '../../catalog/models/product.model';

describe('CartService', () => {
  const headset: Product = {
    id: 1,
    name: 'Auriculares Pro',
    category: 'audio',
    price: 100,
    description: 'Auriculares inalambricos',
    imageUrl: 'https://example.com/headset.jpg',
    stock: 2,
  };

  const mouse: Product = {
    id: 2,
    name: 'Mouse Ergonomico',
    category: 'office',
    price: 40,
    description: 'Mouse para oficina',
    imageUrl: 'https://example.com/mouse.jpg',
    stock: 5,
  };

  let service: CartService;

  beforeEach(() => {
    service = new CartService();
  });

  it('agrega productos y calcula totales correctamente', () => {
    service.addProduct(headset);
    service.addProduct(mouse);

    expect(service.totalItems()).toBe(2);
    expect(service.subtotal()).toBe(140);
    expect(service.tax()).toBe(29.4);
    expect(service.shipping()).toBe(0);
    expect(service.total()).toBe(169.4);
  });

  it('no supera el stock al incrementar cantidad', () => {
    service.addProduct(headset);
    service.addProduct(headset);
    service.addProduct(headset);

    const item = service.items().find((cartItem) => cartItem.product.id === headset.id);

    expect(item?.quantity).toBe(2);
    expect(service.totalItems()).toBe(2);
  });

  it('actualiza cantidad, elimina por cantidad cero y limpia carrito', () => {
    service.addProduct(headset);
    service.addProduct(mouse);

    service.setQuantity(mouse.id, 3);
    expect(service.items().find((item) => item.product.id === mouse.id)?.quantity).toBe(3);

    service.setQuantity(headset.id, 0);
    expect(service.items().some((item) => item.product.id === headset.id)).toBeFalse();

    service.clear();
    expect(service.isEmpty()).toBeTrue();
    expect(service.total()).toBe(0);
  });
});
