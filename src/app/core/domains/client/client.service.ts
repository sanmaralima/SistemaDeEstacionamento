import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { ClientRequest, ClientResponse } from './client.types';

@Injectable({ providedIn: 'root' })
export class ClientService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}clients`;

  create(request: ClientRequest): Observable<ClientResponse> {
    return this.http.post<ClientResponse>(this.baseUrl, request);
  }

  getAll(): Observable<ClientResponse[]> {
    return this.http.get<ClientResponse[]>(this.baseUrl);
  }

  getById(id: string): Observable<ClientResponse> {
    return this.http.get<ClientResponse>(`${this.baseUrl}/${id}`);
  }

  update(id: string, request: ClientRequest): Observable<ClientResponse> {
    return this.http.put<ClientResponse>(`${this.baseUrl}/${id}`, request);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
