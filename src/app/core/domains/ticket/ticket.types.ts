import { VehicleResponse } from '../vehicle/vehicle.types';

export type PaymentMethod = 'DINHEIRO' | 'PIX' | 'CARD_CREDIT' | 'CARD_DEBIT';

export interface TicketRequest {
  plate: string;
  model: string;
  color: string;
  clientId?: string;
}

export interface TicketResponse {
  id: string;
  companyId: string;
  vehicle: VehicleResponse;
  partnershipId?: string;
  enteredAt: string;
  exitedAt?: string;
  status: string;
  totalAmount?: number;
  paymentMethod?: PaymentMethod;
}

export interface ApplyPartnershipParams {
  id: string;
  partnershipId: string;
}

export interface CheckOutParams {
  id: string;
  paymentMethod: PaymentMethod;
}
