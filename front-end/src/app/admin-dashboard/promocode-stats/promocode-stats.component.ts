import { Component, OnInit } from '@angular/core';
import { PromoCodeService } from "../../services/promocode.service";
import {CurrencyPipe, DatePipe, NgForOf} from "@angular/common";

@Component({
  selector: 'app-promocode-stats',
  templateUrl: 'promocode-stats.component.html',
  standalone: true,
  imports: [
    NgForOf,
    CurrencyPipe,
    DatePipe
  ],
  styleUrls: ['promocode-stats.component.scss']
})
export class PromoCodeStatsComponent implements OnInit {
  promoCodeStats: any[] = [];

  constructor(private promoCodeService: PromoCodeService) { }

  ngOnInit(): void {
    this.loadPromoCodeStats();
  }

  loadPromoCodeStats(): void {
    this.promoCodeService.getPromoCodeStats().subscribe(
      stats => {
        this.promoCodeStats = stats;
      },
      error => {
        console.error('Error loading promo code stats:', error);
      }
    );
  }
}
