import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { UserUpdateRequest, UserRoleRequest, UserResponse, RegisterRequest } from './user.types';

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}users`;

  getProfile(): Observable<UserResponse> {
    return this.http.get<UserResponse>(`${this.baseUrl}/profile`);
  }

  getByCompany(companyId: string): Observable<UserResponse[]> {
    return this.http.get<UserResponse[]>(`${this.baseUrl}/company/${companyId}`);
  }

  createCollaborator(companyId: string, data: RegisterRequest): Observable<UserResponse> {
    return this.http.post<UserResponse>(`${this.baseUrl}/company/${companyId}`, data);
  }

  update(id: string, request: UserUpdateRequest): Observable<UserResponse> {
    return this.http.put<UserResponse>(`${this.baseUrl}/${id}`, request);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  updateRole(id: string, request: UserRoleRequest): Observable<UserResponse> {
    return this.http.patch<UserResponse>(`${this.baseUrl}/${id}/role`, request);
  }
}
