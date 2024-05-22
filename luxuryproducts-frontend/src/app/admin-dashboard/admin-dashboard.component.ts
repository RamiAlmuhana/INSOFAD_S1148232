
import { Component, OnInit } from '@angular/core';
import {CurrencyPipe, DatePipe, NgForOf, NgIf} from "@angular/common";
import {Router} from "@angular/router";
@Component({
  selector: 'app-order-history',
  templateUrl: 'admin-dashboard.component.html',
  standalone: true,
  imports: [
    CurrencyPipe,
    DatePipe,
    NgIf,
    NgForOf
  ],
  styleUrls: ['admin-dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit {
  constructor(private router: Router) { }
  ngOnInit(): void {
  }
  navigateToAllPromo() {
    this.router.navigate(['admin/promocode-list']);
  }
  navigateToPromoStats() {
    this.router.navigate(['admin/promocode-stats']);
  }
}
