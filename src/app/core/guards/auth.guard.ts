import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../domains/auth/auth.service';
import { isTokenExpired } from '../utils/jwt';

export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const token = authService.token();

  if (token && !isTokenExpired(token)) {
    return true;
  }

  authService.logout();
  router.navigate(['/login']);
  return false;
};
