// promo-code.model.ts

export interface PromoCode {
  id: number;
  code: string;
  discount: number;
  expiryDate: Date;
  maxUsageCount: number;
  type: PromoCodeType;
}

export enum PromoCodeType {
  FIXED_AMOUNT = 'FIXED_AMOUNT',
  PERCENTAGE = 'PERCENTAGE'
}
