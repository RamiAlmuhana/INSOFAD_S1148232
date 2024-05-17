import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PromoCode } from "../models/promocode.model";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class PromoCodeService {

  private baseUrl = environment.base_url + '/promocodes';

  constructor(private http: HttpClient) { }

  getAllPromoCodes(): Observable<PromoCode[]> {
    return this.http.get<PromoCode[]>(this.baseUrl);
  }

  getPromoCode(id: number): Observable<PromoCode> {
    return this.http.get<PromoCode>(`${this.baseUrl}/${id}`);
  }

  createPromoCode(promoCode: PromoCode): Observable<any> {
    return this.http.post(this.baseUrl, promoCode);
  }

  updatePromoCode(id: number, promoCode: PromoCode): Observable<PromoCode> {
    return this.http.put<PromoCode>(`${this.baseUrl}/${id}`, promoCode);
  }

  deletePromoCode(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

  // New method to fetch promo code statistics
  getPromoCodeStats(): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/promocode-stats`);
  }
}
