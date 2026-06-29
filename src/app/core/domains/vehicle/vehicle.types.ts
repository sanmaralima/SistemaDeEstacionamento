export interface VehicleRequest {
  plate: string;
  model: string;
  color: string;
  clientId?: string;
}

export interface VehicleResponse {
  id: string;
  plate: string;
  model: string;
  color: string;
  clientId?: string;
  companyId: string;
}

export interface UpdateVehicleParams {
  companyId: string;
  id: string;
  request: VehicleRequest;
}
