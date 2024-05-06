import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { PromoCodeService } from "../../../services/promocode.service";

@Component({
  selector: 'app-promocode-add',
  templateUrl: 'promocode-add.component.html',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  styleUrls: ['promocode-add.component.scss']
})
export class PromoCodeAddComponent implements OnInit {
  promoCodeForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private promoCodeService: PromoCodeService) {
    this.promoCodeForm = this.formBuilder.group({
      code: ['', Validators.required],
      discount: ['', [Validators.required, Validators.min(0)]],
      expiryDate: ['', Validators.required],
      maxUsageCount: ['', [Validators.required, Validators.min(1)]],
      type: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.promoCodeForm.invalid) {
      return;
    }

    // Format expiryDate to ISO 8601 string format
    const promoCodeData = { ...this.promoCodeForm.value, expiryDate: new Date(this.promoCodeForm.value.expiryDate).toISOString() };

    this.promoCodeService.createPromoCode(promoCodeData).subscribe(
      () => {
        // Handle successful creation
        console.log('Promo code created successfully.');
        // Reset the form
        this.promoCodeForm.reset();
      },
      (error: any) => {
        // Handle error
        console.error('Error creating promo code:', error);
      }
    );
  }

  ngOnInit(): void {
    // Remove form initialization from here if you initialize in the constructor
  }
}
