import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {Observable} from 'rxjs';

import {environment} from '../../environments/environment';
import {Product} from '../models/product.model';

@Injectable({
  providedIn: 'root'
})
export class ProductsService {


  private baseUrl: string = environment.base_url + "/products";

  constructor(private http: HttpClient) {
  }

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.baseUrl);
  }

  createProduct(productData: any): Observable<any> {
    return this.http.post<any>(this.baseUrl, productData);
  }

  public getProductByIndex(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.baseUrl}/${id}`);
  }

  public DeleteProductByIndex(id: number): Observable<Product> {
    return this.http.delete<Product>(`${this.baseUrl}/${id}`);
  }

  public updateProductByIndex(id: Product, product: number): Observable<Product> {
    return this.http.put<Product>(`${this.baseUrl}/${id}`, product);
  }

}
