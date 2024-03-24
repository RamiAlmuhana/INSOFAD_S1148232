import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Order } from '../models/order.model'; // Pas dit pad aan naar waar je model zich bevindt
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private baseUrl: string = environment.base_url + "/orders/myOrders";

  constructor(private http: HttpClient) { }

  getOrdersByCurrentUser(): Observable<Order[]> {
    return this.http.get<Order[]>(this.baseUrl);
  }
}
