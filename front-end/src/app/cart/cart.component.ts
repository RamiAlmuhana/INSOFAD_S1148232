import {Component, OnInit} from '@angular/core';
import {CurrencyPipe, NgFor, NgIf} from '@angular/common';
import {CartService} from '../services/cart.service';
import {Product} from '../models/product.model';
import {Router} from '@angular/router';
import {AuthService} from "../auth/auth.service";
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CurrencyPipe, NgFor, NgIf],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss'
})
export class CartComponent implements OnInit {
  public products_in_cart: Product[];
  public userIsLoggedIn: boolean = false;
  public amountOfProducts: number = 0;


  constructor(private cartService: CartService, private router: Router, private authService: AuthService, private http: HttpClient) {

  }

  ngOnInit() {
    this.products_in_cart = this.cartService.allProductsInCart();
    this.cartService.$productInCart.subscribe((products: Product[]) => {
      this.products_in_cart = products;
      this.amountOfProducts = products.length;
      this.checkLoginState();

    });
  }

  public clearCart() {
    this.cartService.clearCart();
  }

  public removeProductFromCart(product_index: number) {
    this.cartService.removeProductFromCart(product_index);
  }

  public getTotalPrice(): number {
    return this.products_in_cart.reduce((total, product) => total + product.price * product.amount, 0);
  }

  onInvalidOrder(){
    return this.amountOfProducts === 0;
  }

  onOrder() {
    if (!this.userIsLoggedIn){
      this.router.navigateByUrl('/auth/login');
    }
    else {
      this.router.navigateByUrl('/orders');
    }
  }

  public checkLoginState(): void {
    this.authService
      .$userIsLoggedIn
      .subscribe((loginState: boolean) => {
        this.userIsLoggedIn = loginState;
      });
  }
}
