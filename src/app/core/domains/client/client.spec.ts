import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { firstValueFrom } from 'rxjs';
import { ClientService } from './client.service';
import { ClientRequest, ClientResponse } from './client.types';
import { environment } from '@environments/environment';

const BASE = `${environment.apiUrl}clients`;

const avulsoRequest: ClientRequest = {
  name: 'João Silva', email: 'joao@email.com', phone: '11999990000', type: 'AVULSO',
};

const mensalistaRequest: ClientRequest = {
  name: 'Maria Souza', email: 'maria@email.com', phone: '11988880000', type: 'MENSALISTA',
};

const avulsoResponse: ClientResponse = {
  id: 'cl-1', companyId: 'c-1', name: 'João Silva',
  email: 'joao@email.com', phone: '11999990000', type: 'AVULSO',
};

const mensalistaResponse: ClientResponse = {
  id: 'cl-2', companyId: 'c-1', name: 'Maria Souza',
  email: 'maria@email.com', phone: '11988880000', type: 'MENSALISTA',
};

describe('ClientService', () => {
  let service: ClientService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ClientService, provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(ClientService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('deve criar cliente AVULSO via POST e retornar tipo correto', async () => {
    const promise = firstValueFrom(service.create(avulsoRequest));
    const req = httpMock.expectOne(BASE);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(avulsoRequest);
    req.flush(avulsoResponse);
    expect((await promise).type).toBe('AVULSO');
  });

  it('deve criar cliente MENSALISTA via POST e retornar tipo correto', async () => {
    const promise = firstValueFrom(service.create(mensalistaRequest));
    const req = httpMock.expectOne(BASE);
    expect(req.request.method).toBe('POST');
    req.flush(mensalistaResponse);
    expect((await promise).type).toBe('MENSALISTA');
  });

  it('deve listar todos os clientes via GET', async () => {
    const promise = firstValueFrom(service.getAll());
    const req = httpMock.expectOne(BASE);
    expect(req.request.method).toBe('GET');
    req.flush([avulsoResponse, mensalistaResponse]);
    expect((await promise).length).toBe(2);
  });

  it('deve buscar cliente por ID via GET', async () => {
    const promise = firstValueFrom(service.getById('cl-1'));
    const req = httpMock.expectOne(`${BASE}/cl-1`);
    expect(req.request.method).toBe('GET');
    req.flush(avulsoResponse);
    expect(await promise).toEqual(avulsoResponse);
  });

  it('deve atualizar cliente via PUT com id e payload corretos', async () => {
    const promise = firstValueFrom(service.update('cl-1', avulsoRequest));
    const req = httpMock.expectOne(`${BASE}/cl-1`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(avulsoRequest);
    req.flush(avulsoResponse);
    expect(await promise).toEqual(avulsoResponse);
  });

  it('deve excluir cliente via DELETE', async () => {
    let completed = false;
    service.delete('cl-1').subscribe({ complete: () => { completed = true; } });
    const req = httpMock.expectOne(`${BASE}/cl-1`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
    expect(completed).toBe(true);
  });
});
