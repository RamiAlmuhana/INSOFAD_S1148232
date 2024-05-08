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

  if (!tokenService.isValid()) {
    router.navigate(['/auth/login']); // Als de token niet geldig is, stuur naar login
    return false;
  }

  return authService.getUserRole().pipe(
    map(role => {
      // Controleer of de route admin rechten vereist
      const requiresAdmin = route.data?.['roles']?.includes('admin');
      if (requiresAdmin && role !== 'admin') {
        router.navigate(['/']); // Stuur niet-admins naar de homepage als de route admin rechten vereist
        return false;
      }
      return true; // Toegang toestaan als de roltoets slaagt
    })
  );
};
