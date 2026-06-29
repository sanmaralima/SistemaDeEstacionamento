package com.locuspark.api.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record TariffConfigurationResponse(
        UUID id,
        UUID companyId,
        Integer toleranceMinutes,
        BigDecimal firstHourValue,
        BigDecimal additionalFractionValue,
        BigDecimal overnightFee,
        BigDecimal lostTicketFee
) {}