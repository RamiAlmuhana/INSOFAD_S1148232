import { Component, OnInit } from '@angular/core';
import { CurrencyPipe, NgFor, NgIf } from '@angular/common';
import { CartService } from '../services/cart.service';
import { Product } from '../models/product.model';
import { Router } from '@angular/router';
import { AuthService } from "../auth/auth.service";
import { HttpClient } from '@angular/common/http';
import { environment } from "../../environments/environment";
import { FormsModule } from "@angular/forms";

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CurrencyPipe, NgFor, NgIf, FormsModule],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {
  public products_in_cart: Product[];
  public userIsLoggedIn: boolean = false;
  public amountOfProducts: number = 0;
  promoCode: string = '';
  discount: number = 0;
  promoApplied: boolean = this.checkPromoApplied();
  appliedPromoCode: string = localStorage.getItem('promoCode') || '';
  displayedDiscount: string = localStorage.getItem('displayedDiscount') || '0';
  promoCodeError: boolean = false; // New field for error flag
  promoCodeErrorMessage: string = ''; // New field for error message
  orderError: boolean = false; // New field for order error
  orderErrorMessage: string = ''; // New field for order error message

  constructor(private cartService: CartService, private router: Router, private authService: AuthService, private http: HttpClient) {}

  ngOnInit() {
    this.products_in_cart = this.cartService.allProductsInCart();
    this.cartService.$productInCart.subscribe((products: Product[]) => {
      this.products_in_cart = products;
      this.amountOfProducts = products.length;
      this.checkLoginState();
      if (this.promoApplied) {
        this.discount = parseFloat(this.displayedDiscount);
      }
    });
  }

  public clearCart() {
    this.cartService.clearCart();
    localStorage.removeItem('promoCode');
    localStorage.removeItem('promoApplied');
    localStorage.removeItem('discountValue');
    localStorage.removeItem('discountType');
    localStorage.removeItem('displayedDiscount');
    this.cartService.removeDiscount();
    this.promoApplied = false;
    this.discount = 0;
    this.appliedPromoCode = '';
    this.promoCodeError = false; // Reset error flag
    this.orderError = false; // Reset order error flag
  }

  public removeProductFromCart(product_index: number) {
    this.cartService.removeProductFromCart(product_index);
  }

  public getTotalPrice(): number {
    return this.cartService.calculateTotalPrice();
  }

  public getTotalPriceWithDiscount(): number {
    return this.cartService.totalPriceWithDiscount;
  }

  onInvalidOrder() {
    return this.amountOfProducts === 0;
  }

  onOrder() {
    if (!this.userIsLoggedIn) {
      this.orderError = true;
      this.orderErrorMessage = 'You need to be logged in to place an order.';
      this.router.navigateByUrl('/auth/login');
    } else {
      this.orderError = false;
      this.router.navigateByUrl('/orders');
    }
  }

  public checkLoginState(): void {
    this.authService.$userIsLoggedIn.subscribe((loginState: boolean) => {
      this.userIsLoggedIn = loginState;
    });
  }

  applyPromoCode() {
    if (this.promoApplied) {
      this.promoCodeError = true;
      this.promoCodeErrorMessage = 'You can only use one promo code per order.';
      return;
    }

    if (this.products_in_cart.length === 0) {
      this.promoCodeError = true;
      this.promoCodeErrorMessage = 'No products found';
      return;
    }

    const url = `${environment.base_url}/promocodes/validate?code=${this.promoCode}`;
    this.http.get<{ discount: number, type: string, minSpendAmount: number }>(url).subscribe({
      next: (response) => {
        const total = this.getTotalPrice();
        if (total >= response.minSpendAmount) {
          this.discount = response.discount;
          this.cartService.applyDiscount(this.discount, response.type as 'FIXED_AMOUNT' | 'PERCENTAGE', this.promoCode);
          this.promoApplied = true;
          this.appliedPromoCode = this.promoCode;
          this.promoCodeError = false; // Reset error flag
        } else {
          this.promoCodeError = true;
          this.promoCodeErrorMessage = `Minimum spend amount for this promo code is ${response.minSpendAmount}`;
        }
      },
      error: () => {
        this.promoCodeError = true;
        this.promoCodeErrorMessage = 'Invalid or expired promo code!';
      }
    });
  }

  removePromoCode() {
    this.cartService.removeDiscount();
    this.promoApplied = false;
    this.discount = 0;
    this.appliedPromoCode = '';
    this.promoCodeError = false; // Reset error flag
  }

  private checkPromoApplied(): boolean {
    return localStorage.getItem('promoApplied') === 'true';
  }
}
