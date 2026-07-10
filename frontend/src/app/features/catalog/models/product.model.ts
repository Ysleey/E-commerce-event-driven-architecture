export interface Product {
  id: number;
  name: string;
  category: string;
  price: number;
  description: string;
  badge?: string;
  imageUrl: string;
  searchTerms?: string[];
  stock: number;
}
