import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Product} from "../models/product.model";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  @Input() public product!: Product;
  @Output() public onBuyProduct: EventEmitter<Product> = new EventEmitter<Product>();

  public buyProduct(product: Product) {
    this.onBuyProduct.emit(product);
  }
}
