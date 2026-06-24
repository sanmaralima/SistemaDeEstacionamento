import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { ReportResponse } from './report.types';

@Injectable({ providedIn: 'root' })
export class ReportService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}reports`;

  getMetrics(companyId: string): Observable<ReportResponse> {
    return this.http.get<ReportResponse>(`${this.baseUrl}`, {
      params: { companyId },
    });
  }
}
