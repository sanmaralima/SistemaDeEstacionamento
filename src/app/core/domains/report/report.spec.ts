import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { firstValueFrom } from 'rxjs';
import { ReportService } from './report.service';
import { ReportResponse } from './report.types';
import { environment } from '@environments/environment';

const BASE = `${environment.apiUrl}reports`;

const mockReportResponse: ReportResponse = {
  totalRevenue: 1500.5,
  totalServices: 120,
  averageStayMinutes: 45,
  paymentMethodSummaries: [
    { paymentMethod: 'DINHEIRO', revenue: 500, count: 40 },
    { paymentMethod: 'PIX', revenue: 600, count: 50 },
    { paymentMethod: 'CARD_CREDIT', revenue: 300, count: 20 },
    { paymentMethod: 'CARD_DEBIT', revenue: 100.5, count: 10 },
  ],
};

describe('ReportService', () => {
  let service: ReportService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ReportService, provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(ReportService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('deve buscar métricas de relatório via GET com query parameter companyId', async () => {
    const companyId = 'c-1';
    const promise = firstValueFrom(service.getMetrics(companyId));
    const req = httpMock.expectOne((r) => r.url === BASE && r.params.get('companyId') === companyId);
    expect(req.request.method).toBe('GET');
    req.flush(mockReportResponse);
    expect(await promise).toEqual(mockReportResponse);
  });
});
