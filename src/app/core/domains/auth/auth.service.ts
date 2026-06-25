import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '@environments/environment';
import { LoginRequest, RegisterRequest, AuthResponse } from './auth.types';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}auth`;

  readonly token = signal<string | null>(localStorage.getItem('token'));
  readonly companyId = signal<string | null>(localStorage.getItem('companyId'));

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, request).pipe(
      tap((response) => {
        this.token.set(response.token);
        localStorage.setItem('token', response.token);
      })
    );
  }

  register(request: RegisterRequest): Observable<unknown> {
    return this.http.post(`${this.baseUrl}/register`, request, { responseType: 'text' });
  }

  logout(): void {
    this.token.set(null);
    this.companyId.set(null);
    localStorage.removeItem('token');
    localStorage.removeItem('companyId');
  }
}
