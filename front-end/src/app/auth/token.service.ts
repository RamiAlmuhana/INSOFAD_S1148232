import {Injectable} from '@angular/core';
import {JwtPayload} from './jwt-payload.model';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  private _localStorageTokenKey: string = 'token';

  constructor() {
  }

  public storeToken(token: string) {
    localStorage.setItem(this._localStorageTokenKey, token);
  }

  public loadToken(): string | null {
    return localStorage.getItem(this._localStorageTokenKey);
  }

  private getPayload(token: string): JwtPayload {
    const jwtPayload = token.split('.')[1];
    return JSON.parse(atob(jwtPayload));
  }

  private tokenExpired(token: string): boolean {
    const expiry = this.getPayload(token).exp;
    return (Math.floor((new Date).getTime() / 1000)) >= expiry;
  }

  public removeToken() {
    localStorage.removeItem(this._localStorageTokenKey);
  }

  public isValid(): boolean {
    const token: string | null = this.loadToken();

    if (!token) {
      return false;
    }

    if (this.tokenExpired(token)) {
      this.removeToken();
      return false;
    }

    // Hier ook andere validaties op de token
    // bijvoorbeeld validatie of de issuer (iss) overeen komt..

    return true;
  }

}
