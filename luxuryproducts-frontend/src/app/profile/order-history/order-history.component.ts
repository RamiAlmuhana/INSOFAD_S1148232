import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../services/order.service'; // Pas dit pad aan naar waar je service zich bevindt
import { Order } from '../../models/order.model'; // Pas dit pad aan naar waar je model zich bevindt
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-order-history',
  templateUrl: './order-history.component.html',
  imports: [CommonModule],
  standalone: true,
  styleUrls: ['./order-history.component.scss']
})
export class OrderHistoryComponent implements OnInit {
  orders: Order[]; // Zorg ervoor dat dit het juiste model gebruikt

  constructor(private orderService: OrderService) { }

  ngOnInit() {
    this.loadOrdersIn();
  }

  loadOrdersIn() {
    this.orderService.getOrdersByCurrentUser().subscribe(orders => {
      this.orders = orders;
    });
  }

  calculateTotal(products: any[]): number {
    let total = 0;
    for (const product of products) {
      total += product.price;
    }
    return total;
  }
}
