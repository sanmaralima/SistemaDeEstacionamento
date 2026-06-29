import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { TicketResponse, PaymentMethod } from './ticket.types';

@Injectable({ providedIn: 'root' })
export class TicketService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}tickets`;

  checkIn(vehicleId: string): Observable<TicketResponse> {
    return this.http.post<TicketResponse>(
      `${this.baseUrl}/check-in?vehicleId=${vehicleId}`,
      {}
    );
  }

  checkOut(id: string, paymentMethod: PaymentMethod): Observable<TicketResponse> {
    return this.http.post<TicketResponse>(
      `${this.baseUrl}/${id}/check-out?paymentMethod=${paymentMethod}`,
      { paymentMethod }
    );
  }

  getAll(): Observable<TicketResponse[]> {
    return this.http.get<TicketResponse[]>(this.baseUrl);
  }

  getById(id: string): Observable<TicketResponse> {
    return this.http.get<TicketResponse>(`${this.baseUrl}/${id}`);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  applyPartnership(id: string, partnershipId: string): Observable<TicketResponse> {
    return this.http.patch<TicketResponse>(
      `${this.baseUrl}/${id}/partnership?partnershipId=${partnershipId}`,
      {}
    );
  }
}
