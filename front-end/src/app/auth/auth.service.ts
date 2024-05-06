import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {AuthResponse} from './auth-response.model';
import {BehaviorSubject, map, Observable, tap} from 'rxjs';
import {AuthRequest} from './auth-request.model';
import {TokenService} from './token.service';
import {User} from "../models/user.model";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private _loginEndpoint: string = 'http://localhost:8080/api/auth/login';
  private _registerEndpoint: string = 'http://localhost:8080/api/auth/register';
  private _currentUserEndpoint: string = 'http://localhost:8080/api/auth/user';

  public $userIsLoggedIn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  constructor(private http: HttpClient, private tokenService: TokenService) {
    if (this.tokenService.isValid()) {
      this.$userIsLoggedIn.next(true);
    }
  }


  public login(authRequest: AuthRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(this._loginEndpoint, authRequest)
      .pipe(
        tap((authResponse: AuthResponse) => {
          this.tokenService.storeToken(authResponse.token);
          this.$userIsLoggedIn.next(true);
        })
      );
  }

  public register(authRequest: AuthRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(this._registerEndpoint, authRequest)
      .pipe(
        tap((authResponse: AuthResponse) => {
          this.tokenService.storeToken(authResponse.token);
          this.$userIsLoggedIn.next(true);
        })
      );
  }

  public logOut(): void {
    this.tokenService.removeToken();
    this.$userIsLoggedIn.next(false);
  }


  public getCurrentUser(): Observable<User> {
    return this.http.get<User>(this._currentUserEndpoint);
  }

  public getUserRole(): Observable<string> {
    return this.getCurrentUser().pipe(
      map(user => user.role)
    );
  }

}
