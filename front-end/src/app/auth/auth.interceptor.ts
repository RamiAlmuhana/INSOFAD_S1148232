import {HttpInterceptorFn} from '@angular/common/http';
import {TokenService} from './token.service';
import {inject} from '@angular/core';
import {Token} from '@angular/compiler';

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const tokenService: TokenService = inject(TokenService);

  if (tokenService.isValid()) {
    const authReq = req.clone({
      headers: req.headers.set('Authorization', 'Bearer ' + tokenService.loadToken())
    });

    return next(authReq);

  }

  return next(req);
};
