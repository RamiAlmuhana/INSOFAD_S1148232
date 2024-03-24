import { Component } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-payment-successful',
  standalone: true,
  imports: [],
  templateUrl: './payment-successful.component.html',
  styleUrl: './payment-successful.component.scss'
})
export class PaymentSuccessfulComponent {

  constructor(private router : Router) {
  }

  backToHome() {
    this.router.navigateByUrl('order-history');
  }
}
