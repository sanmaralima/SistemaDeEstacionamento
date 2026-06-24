import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { firstValueFrom } from 'rxjs';
import { UserService } from './user.service';
import { UserUpdateRequest, UserRoleRequest, UserResponse } from './user.types';
import { environment } from '@environments/environment';

const BASE = `${environment.apiUrl}users`;

const mockUser: UserResponse = {
  id: 'u-1', name: 'Admin Parque', email: 'admin@park.com', role: 'ADMIN', companyId: 'c-1',
};
const updateRequest: UserUpdateRequest = { name: 'Admin Atualizado', email: 'novo@park.com' };
const roleRequest: UserRoleRequest = { role: 'OPERATOR' };

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UserService, provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('deve buscar perfil do usuário autenticado via GET /users/profile', async () => {
    const promise = firstValueFrom(service.getProfile());
    const req = httpMock.expectOne(`${BASE}/profile`);
    expect(req.request.method).toBe('GET');
    req.flush(mockUser);
    expect(await promise).toEqual(mockUser);
  });

  it('deve listar usuários da empresa via GET /users/company/{companyId}', async () => {
    const promise = firstValueFrom(service.getByCompany('c-1'));
    const req = httpMock.expectOne(`${BASE}/company/c-1`);
    expect(req.request.method).toBe('GET');
    req.flush([mockUser]);
    expect(await promise).toEqual([mockUser]);
  });

  it('deve atualizar usuário via PUT com id e payload corretos', async () => {
    const updated = { ...mockUser, ...updateRequest };
    const promise = firstValueFrom(service.update('u-1', updateRequest));
    const req = httpMock.expectOne(`${BASE}/u-1`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(updateRequest);
    req.flush(updated);
    expect(await promise).toEqual(updated);
  });

  it('deve excluir usuário via DELETE', async () => {
    let completed = false;
    service.delete('u-1').subscribe({ complete: () => { completed = true; } });
    const req = httpMock.expectOne(`${BASE}/u-1`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
    expect(completed).toBe(true);
  });

  it('deve alterar papel do usuário via PATCH /users/{id}/role', async () => {
    const updated = { ...mockUser, role: 'OPERATOR' };
    const promise = firstValueFrom(service.updateRole('u-1', roleRequest));
    const req = httpMock.expectOne(`${BASE}/u-1/role`);
    expect(req.request.method).toBe('PATCH');
    expect(req.request.body).toEqual(roleRequest);
    req.flush(updated);
    expect((await promise).role).toBe('OPERATOR');
  });
});
