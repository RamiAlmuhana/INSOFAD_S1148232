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
  public totalDiscount: number = 0;
  public totalPriceWithDiscount: number = this.loadInitialDiscountedPrice();
  private baseUrl: string = environment.base_url + "/orders";


  constructor(private http: HttpClient) {
    this.loadProductsFromLocalStorage();
    this.reapplyDiscountIfApplicable();
  }

  private reapplyDiscountIfApplicable() {
    const discountValue = parseFloat(localStorage.getItem('discountValue') || '0');
    const discountType = localStorage.getItem('discountType') as 'FIXED_AMOUNT' | 'PERCENTAGE' | null;

    if (discountType && discountValue) {
      this.applyDiscount(discountValue, discountType);
    }
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

  // Pas korting toe zonder de productprijzen te veranderen
  public applyDiscount(discountValue: number, discountType: 'FIXED_AMOUNT' | 'PERCENTAGE') {
    const total = this.calculateTotalPrice();
    if (discountType === 'FIXED_AMOUNT') {
      this.totalDiscount = discountValue;
    } else if (discountType === 'PERCENTAGE') {
      this.totalDiscount = total * (discountValue / 100);
    }
    this.totalPriceWithDiscount = total - this.totalDiscount;
    localStorage.setItem('promoApplied', 'true');
    localStorage.setItem('discountValue', discountValue.toString());
    localStorage.setItem('discountType', discountType);
    this.$productInCart.next(this.productsInCart.slice());
  }

  // Verwijder de korting
  public removeDiscount() {
    this.totalDiscount = 0;
    this.totalPriceWithDiscount = this.calculateTotalPrice();
    localStorage.removeItem('promoApplied');
    localStorage.removeItem('discountValue');
    localStorage.removeItem('discountType');
    this.$productInCart.next(this.productsInCart.slice());
  }

  // Bereken de totale prijs zonder korting
  public calculateTotalPrice(): number {
    return this.productsInCart.reduce((total, product) => total + product.price * product.amount, 0);
  }

  private loadInitialDiscountedPrice(): number {
    const total = this.calculateTotalPrice();
    const discountValue = parseFloat(localStorage.getItem('discountValue') || '0');
    const discountType = localStorage.getItem('discountType') as 'FIXED_AMOUNT' | 'PERCENTAGE' | null;

    if (discountType === 'FIXED_AMOUNT') {
      return Math.max(0, total - discountValue);
    } else if (discountType === 'PERCENTAGE') {
      return Math.max(0, total - (total * discountValue / 100));
    }
    return total;
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
