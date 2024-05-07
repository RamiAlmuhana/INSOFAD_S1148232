import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, catchError, throwError } from 'rxjs';
import { Product } from '../models/product.model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Order } from '../models/order.model';

const localStorageKey: string = "products-in-cart";
const promoAppliedKey: string = "promoApplied";

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private productsInCart: Product[] = [];
  public $productInCart: BehaviorSubject<Product[]> = new BehaviorSubject<Product[]>([]);
  private baseUrl: string = environment.base_url + "/orders";

  constructor(private http: HttpClient) {
    this.loadProductsFromLocalStorage();
  }

  public addProductToCart(productToAdd: Product) {
    let existingProductIndex: number = this.productsInCart.findIndex(product => product.name === productToAdd.name);
    if (existingProductIndex !== -1) {
      this.productsInCart[existingProductIndex].amount += 1;
    } else {
      productToAdd.amount = 1;
      this.productsInCart.push(productToAdd);
    }
    this.saveProductsAndNotifyChange();
  }

  public removeProductFromCart(productIndex: number) {
    if (this.productsInCart[productIndex].amount > 1) {
      this.productsInCart[productIndex].amount -= 1;
    } else {
      this.productsInCart.splice(productIndex, 1);
    }
    this.saveProductsAndNotifyChange();
    localStorage.removeItem(promoAppliedKey);
  }

  public clearCart() {
    this.productsInCart = [];
    localStorage.removeItem(promoAppliedKey);  // Reset promo code status on cart clear
    this.saveProductsAndNotifyChange();
  }

  public allProductsInCart(): Product[] {
    return this.productsInCart.slice();
  }

  public addOrder(order: Order): Observable<Order> {
    console.log("Ontvangen order: " + order);
    return this.http.post<Order>(this.baseUrl, order).pipe(
      catchError(error => {
        console.error('Error adding order:', error);
        return throwError(error);
      })
    );
  }

  applyDiscount(discountValue: number, discountType: 'FIXED_AMOUNT' | 'PERCENTAGE') {
    if (discountType === 'FIXED_AMOUNT') {
      this.productsInCart.forEach(product => {
        const newPrice = product.price - discountValue;
        product.price = newPrice > 0 ? newPrice : 0;
      });
    } else if (discountType === 'PERCENTAGE') {
      this.productsInCart.forEach(product => {
        const newPrice = product.price * (1 - discountValue);
        product.price = newPrice > 0 ? newPrice : 0;
      });
    }
    localStorage.setItem(promoAppliedKey, 'true');  // Save promo code applied status
    this.saveProductsAndNotifyChange();
  }

  // ------------ PRIVATE ------------------
  private saveProductsAndNotifyChange(): void {
    this.saveProductsToLocalStorage(this.productsInCart.slice());
    this.$productInCart.next(this.productsInCart.slice());
  }

  private saveProductsToLocalStorage(products: Product[]): void {
    localStorage.setItem(localStorageKey, JSON.stringify(products));
  }

  private loadProductsFromLocalStorage(): void {
    let productsOrNull = localStorage.getItem(localStorageKey);
    if (productsOrNull != null) {
      let products: Product[] = JSON.parse(productsOrNull);
      this.productsInCart = products;
      this.$productInCart.next(this.productsInCart.slice());
    }
  }
}
