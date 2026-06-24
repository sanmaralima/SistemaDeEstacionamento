export interface PaymentMethodSummary {
  paymentMethod: string;
  revenue: number;
  count: number;
}

export interface ReportResponse {
  totalRevenue: number;
  totalServices: number;
  averageStayMinutes: number;
  paymentMethodSummaries: PaymentMethodSummary[];
}
