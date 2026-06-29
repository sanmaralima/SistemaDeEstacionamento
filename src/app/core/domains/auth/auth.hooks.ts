import { inject } from '@angular/core';
import { injectMutation, QueryClient } from '@tanstack/angular-query-experimental';
import { lastValueFrom } from 'rxjs';
import { AuthService } from './auth.service';
import { LoginRequest, RegisterRequest } from './auth.types';

export function useLoginMutation() {
  const service = inject(AuthService);
  const queryClient = inject(QueryClient);
  return injectMutation(() => ({
    mutationFn: (request: LoginRequest) => lastValueFrom(service.login(request)),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['profile'] }),
  }));
}

export function useRegisterMutation() {
  const service = inject(AuthService);
  return injectMutation(() => ({
    mutationFn: (request: RegisterRequest) => lastValueFrom(service.register(request)),
  }));
}
