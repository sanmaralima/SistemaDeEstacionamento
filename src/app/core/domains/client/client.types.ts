export interface ClientRequest {
  name: string;
  email: string;
  phone: string;
  type: 'AVULSO' | 'MENSALISTA';
}

export interface ClientResponse {
  id: string;
  companyId: string;
  name: string;
  email: string;
  phone: string;
  type: 'AVULSO' | 'MENSALISTA';
}

export interface UpdateClientParams {
  id: string;
  request: ClientRequest;
}
