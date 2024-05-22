import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PromoCodeService } from "../../../services/promocode.service";
import { CategoryService } from "../../../services/category.service";
import { PromoCode } from "../../../models/promocode.model";
import { Category } from "../../../models/category.model";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-promocode-update',
  templateUrl: 'promocode-update.component.html',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgForOf
  ],
  styleUrls: ['promocode-update.component.scss']
})
export class PromocodeUpdateComponent implements OnInit {
  promoCodeForm: FormGroup;
  promoCodeId: number;
  categories: Category[] = [];

  constructor(
    private formBuilder: FormBuilder,
    private promoCodeService: PromoCodeService,
    private categoryService: CategoryService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.promoCodeForm = this.formBuilder.group({
      id: [''],
      code: ['', Validators.required],
      discount: ['', [Validators.required, Validators.min(0)]],
      expiryDate: ['', Validators.required],
      startDate: ['', Validators.required],
      maxUsageCount: ['', [Validators.required, Validators.min(1)]],
      minSpendAmount: ['', [Validators.required, Validators.min(0)]],
      type: ['', Validators.required],
      categoryId: [null]
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.promoCodeId = +params['id'];
      if (this.promoCodeId) {
        this.loadPromoCode(this.promoCodeId);
      }
    });
    this.loadCategories();
  }

  loadPromoCode(id: number): void {
    this.promoCodeService.getPromoCode(id).subscribe(
      promoCode => {
        this.promoCodeForm.patchValue({
          ...promoCode,
          categoryId: promoCode.category ? promoCode.category.id : null
        }); // Populate form with promo code details
      },
      error => {
        console.error('Error loading promo code:', error);
      }
    );
  }

  loadCategories(): void {
    this.categoryService.getAllCategories().subscribe(
      (categories) => {
        this.categories = categories;
      },
      (error) => {
        console.error('Error loading categories:', error);
      }
    );
  }

  onSubmit(): void {
    if (this.promoCodeForm.invalid) {
      return;
    }
    const formValue = this.promoCodeForm.value;

    const promoCodeData = {
      ...formValue,
      expiryDate: new Date(formValue.expiryDate).toISOString(),
      startDate: new Date(formValue.startDate).toISOString(),
      category: formValue.categoryId ? { id: formValue.categoryId } : null
    };

    this.promoCodeService.updatePromoCode(this.promoCodeId, promoCodeData).subscribe(
      (updatedPromoCode: PromoCode) => {
        console.log('Promo code updated successfully:', updatedPromoCode);
        // Redirect to promo code list after successful update
        this.router.navigate(['admin/promocode-list']);
      },
      (error: any) => {
        console.error('Error updating promo code:', error);
      }
    );
  }
}
