export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  username: string;
  password: string;
  companyName: string;
  cnpj?: string;
  totalSpots?: number;
}

export interface AuthResponse {
  token: string;
  id: string;
  username: string;
  role: string;
  companyId?: string;
}
