import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PromoCodeService } from "../../../services/promocode.service";
import { PromoCode } from "../../../models/promocode.model";

@Component({
  selector: 'app-promocode-update',
  templateUrl: 'promocode-update.component.html',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  styleUrls: ['promocode-update.component.scss']
})
export class PromocodeUpdateComponent implements OnInit {
  promoCodeForm: FormGroup;
  promoCodeId: number;

  constructor(
    private formBuilder: FormBuilder,
    private promoCodeService: PromoCodeService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.promoCodeForm = this.formBuilder.group({
      id: [''],
      code: ['', Validators.required],
      discount: ['', [Validators.required, Validators.min(0)]],
      expiryDate: ['', Validators.required],
      maxUsageCount: ['', [Validators.required, Validators.min(1)]],
      type: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.promoCodeId = +params['id'];
      if (this.promoCodeId) {
        this.loadPromoCode(this.promoCodeId);
      }
    });
  }

  loadPromoCode(id: number): void {
    this.promoCodeService.getPromoCode(id).subscribe(
      promoCode => {
        this.promoCodeForm.patchValue(promoCode); // Populate form with promo code details
      },
      error => {
        console.error('Error loading promo code:', error);
      }
    );
  }

  onSubmit(): void {
    if (this.promoCodeForm.invalid) {
      return;
    }
    const promoCodeData = this.promoCodeForm.value;
    this.promoCodeService.updatePromoCode(this.promoCodeId, promoCodeData).subscribe(
      (updatedPromoCode: PromoCode) => {
        console.log('Promo code updated successfully:', updatedPromoCode);
        // Redirect to promo code list after successful update
        this.router.navigate(['/promocode-list']);
      },
      (error: any) => {
        console.error('Error updating promo code:', error);
      }
    );
  }
}
