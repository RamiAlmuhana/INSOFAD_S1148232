// promo-code.model.ts

export interface PromoCode {
  id: number;
  code: string;
  discount: number;
  expiryDate: Date;
  startDate: Date; // New field
  maxUsageCount: number;
  type: PromoCodeType;
  minSpendAmount: number;
}

export enum PromoCodeType {
  FIXED_AMOUNT = 'FIXED_AMOUNT',
  PERCENTAGE = 'PERCENTAGE'
}
