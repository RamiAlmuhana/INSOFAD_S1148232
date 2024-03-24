import {Injectable} from "@angular/core";
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {User} from "../models/user.model";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl: string = environment.base_url + "/auth/user";

  constructor(private http: HttpClient) {
  }

  public getUserByEmail(): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}`);
  }

  public updateUser(user: User): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}`, user);
  }

  public deleteUser(email: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${email}`);
  }
}
