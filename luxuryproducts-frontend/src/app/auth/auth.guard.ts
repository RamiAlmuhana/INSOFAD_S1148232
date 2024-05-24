import {CanActivateFn} from '@angular/router';
import {TokenService} from './token.service';
import {inject} from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
  // heeft iemand een geldige token?

  const tokenService: TokenService = inject(TokenService);

  if (tokenService.isValid()) {
    return true;
  }

  return false;
};
