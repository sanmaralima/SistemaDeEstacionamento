export interface TariffConfigurationRequest {
  firstHourRate: number;
  additionalHourRate: number;
  gracePeriodMinutes: number;
}

export interface TariffConfigurationResponse {
  id: string;
  companyId: string;
  toleranceMinutes: number;
  firstHourValue: number;
  additionalFractionValue: number;
  overnightFee: number;
  lostTicketFee: number;
}

export interface PricingConfigurationRequest {
  timeFractioningMinutes: number;
  monthlyMemberFee: number;
  overnightStayFee: number;
}

export interface PricingConfigurationResponse {
  id: string;
  companyId: string;
  dailyTriggerHours: number;
  dailyValue: number;
  monthlyBaseValue: number;
}
