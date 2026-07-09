export interface Product {
  id: number;
  name: string;
  category: 'audio' | 'lifestyle' | 'gaming' | 'office';
  price: number;
  description: string;
  badge?: string;
  imageUrl: string;
  stock: number;
}
