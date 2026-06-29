import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { PartnershipRequest, PartnershipResponse } from './partnership.types';

@Injectable({ providedIn: 'root' })
export class PartnershipService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}partnerships`;

  listAll(): Observable<PartnershipResponse[]> {
    return this.http.get<PartnershipResponse[]>(this.baseUrl);
  }

  getById(id: string): Observable<PartnershipResponse> {
    return this.http.get<PartnershipResponse>(`${this.baseUrl}/${id}`);
  }

  create(request: PartnershipRequest): Observable<PartnershipResponse> {
    return this.http.post<PartnershipResponse>(this.baseUrl, request);
  }

  update(id: string, request: PartnershipRequest): Observable<PartnershipResponse> {
    return this.http.put<PartnershipResponse>(`${this.baseUrl}/${id}`, request);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
