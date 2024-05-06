import { CanActivateFn } from '@angular/router';
import { TokenService } from './token.service';
import { AuthService } from './auth.service'; // Importeer de AuthService
import { Router } from '@angular/router'; // Importeer de Router
import { inject } from '@angular/core';
import { map } from 'rxjs/operators'; // Importeer map operator

export const authGuard: CanActivateFn = (route, state) => {
  // heeft iemand een geldige token?

  const tokenService: TokenService = inject(TokenService);
  const authService: AuthService = inject(AuthService);
  const router: Router = inject(Router);

  if (tokenService.isValid()) {
    // Gebruiker heeft een geldige token, controleer de rol
    return authService.getUserRole().pipe(
      map(role => {
        if (role === 'admin') {
          return true; // Gebruiker is een admin, geef toegang
        } else {
          router.navigate(['/']); // Gebruiker is geen admin, stuur ze naar de homepage of een andere pagina
          return false;
        }
      })
    );
  }

  return false; // Gebruiker heeft geen geldige token, geen toegang
};
