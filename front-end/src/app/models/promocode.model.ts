// promo-code.model.ts

import {Category} from "./category.model";

export interface PromoCode {
  id: number;
  code: string;
  discount: number;
  expiryDate: Date;
  startDate: Date; // New field
  maxUsageCount: number;
  type: PromoCodeType;
  minSpendAmount: number;
  category?: Category; // New field for category
}

export enum PromoCodeType {
  FIXED_AMOUNT = 'FIXED_AMOUNT',
  PERCENTAGE = 'PERCENTAGE'
}
