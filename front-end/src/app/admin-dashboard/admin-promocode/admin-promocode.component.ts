import { Component, OnInit } from '@angular/core';
import { PromoCode } from "../../models/promocode.model";
import { PromoCodeService } from "../../services/promocode.service";
import {RouterLink} from "@angular/router";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-promo-code-list',
  templateUrl: 'admin-promocode.component.html',
  standalone: true,
  imports: [
    RouterLink,
    NgForOf
  ],
  styleUrls: ['admin-promocode.component.scss']
})
export class AdminPromocodeComponent implements OnInit {
  promoCodes: PromoCode[] = [];

  constructor(private promoCodeService: PromoCodeService) { }

  ngOnInit(): void {
    this.loadPromoCodes();
  }

  loadPromoCodes(): void {
    this.promoCodeService.getAllPromoCodes().subscribe(
      promoCodes => {
        this.promoCodes = promoCodes;
      },
      error => {
        console.error('Error loading promo codes:', error);
      }
    );
  }

  deletePromoCode(id: number): void {
    if (confirm('Are you sure you want to delete this promo code?')) {
      this.promoCodeService.deletePromoCode(id).subscribe(
        () => {
          // Remove the deleted promo code from the list
          this.promoCodes = this.promoCodes.filter(promoCode => promoCode.id !== id);
          console.log('Promo code deleted successfully');
        },
        error => {
          console.error('Error deleting promo code:', error);
        }
      );
    }
  }
}
