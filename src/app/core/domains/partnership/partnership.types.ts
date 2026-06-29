export interface PartnershipRequest {
  name: string;
  discountType: string;
  value: number;
}

export interface PartnershipResponse {
  id: string;
  companyId: string;
  name: string;
  discountType: string;
  value: number;
}

export interface UpdatePartnershipParams {
  id: string;
  request: PartnershipRequest;
}
