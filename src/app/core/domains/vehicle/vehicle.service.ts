import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { VehicleRequest, VehicleResponse } from './vehicle.types';

@Injectable({ providedIn: 'root' })
export class VehicleService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = environment.apiUrl;

  private buildUrl(companyId: string): string {
    return `${this.apiUrl}companies/${companyId}/vehicles`;
  }

  create(companyId: string, request: VehicleRequest): Observable<VehicleResponse> {
    return this.http.post<VehicleResponse>(this.buildUrl(companyId), request);
  }

  listAll(companyId: string): Observable<VehicleResponse[]> {
    return this.http.get<VehicleResponse[]>(this.buildUrl(companyId));
  }

  getById(companyId: string, id: string): Observable<VehicleResponse> {
    return this.http.get<VehicleResponse>(`${this.buildUrl(companyId)}/${id}`);
  }

  update(companyId: string, id: string, request: VehicleRequest): Observable<VehicleResponse> {
    return this.http.put<VehicleResponse>(`${this.buildUrl(companyId)}/${id}`, request);
  }

  delete(companyId: string, id: string): Observable<void> {
    return this.http.delete<void>(`${this.buildUrl(companyId)}/${id}`);
  }
}
