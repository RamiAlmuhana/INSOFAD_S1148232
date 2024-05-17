import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { PromoCodeService } from "../../../services/promocode.service";
import { CategoryService } from "../../../services/category.service";
import { Category } from "../../../models/category.model";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-promocode-add',
  templateUrl: 'promocode-add.component.html',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgForOf
  ],
  styleUrls: ['promocode-add.component.scss']
})
export class PromoCodeAddComponent implements OnInit {
  promoCodeForm: FormGroup;
  categories: Category[] = [];

  constructor(
    private formBuilder: FormBuilder,
    private promoCodeService: PromoCodeService,
    private categoryService: CategoryService
  ) {
    this.promoCodeForm = this.formBuilder.group({
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
    this.loadCategories();
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

    this.promoCodeService.createPromoCode(promoCodeData).subscribe(
      () => {
        console.log('Promo code created successfully.');
        this.promoCodeForm.reset();
      },
      (error: any) => {
        console.error('Error creating promo code:', error);
      }
    );
  }
}
