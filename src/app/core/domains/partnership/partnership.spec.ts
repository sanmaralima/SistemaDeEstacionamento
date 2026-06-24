import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { firstValueFrom } from 'rxjs';
import { PartnershipService } from './partnership.service';
import { PartnershipRequest, PartnershipResponse } from './partnership.types';
import { environment } from '@environments/environment';

const BASE = `${environment.apiUrl}partnerships`;

const mockRequest: PartnershipRequest = { name: 'Farmácia Popular', discountType: 'PERCENTAGE', value: 20 };
const mockResponse: PartnershipResponse = {
  id: 'p-1', companyId: 'c-1', name: 'Farmácia Popular', discountType: 'PERCENTAGE', value: 20,
};

describe('PartnershipService', () => {
  let service: PartnershipService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PartnershipService, provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(PartnershipService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('deve listar todas as parcerias via GET', async () => {
    const promise = firstValueFrom(service.listAll());
    const req = httpMock.expectOne(BASE);
    expect(req.request.method).toBe('GET');
    req.flush([mockResponse]);
    expect(await promise).toEqual([mockResponse]);
  });

  it('deve buscar parceria por ID via GET', async () => {
    const promise = firstValueFrom(service.getById('p-1'));
    const req = httpMock.expectOne(`${BASE}/p-1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
    expect(await promise).toEqual(mockResponse);
  });

  it('deve criar parceria via POST com payload correto', async () => {
    const promise = firstValueFrom(service.create(mockRequest));
    const req = httpMock.expectOne(BASE);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockRequest);
    req.flush(mockResponse);
    expect(await promise).toEqual(mockResponse);
  });

  it('deve atualizar parceria via PUT com id e payload corretos', async () => {
    const promise = firstValueFrom(service.update('p-1', mockRequest));
    const req = httpMock.expectOne(`${BASE}/p-1`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(mockRequest);
    req.flush(mockResponse);
    expect(await promise).toEqual(mockResponse);
  });

  it('deve excluir parceria via DELETE', async () => {
    let completed = false;
    service.delete('p-1').subscribe({ complete: () => { completed = true; } });
    const req = httpMock.expectOne(`${BASE}/p-1`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
    expect(completed).toBe(true);
  });
});
