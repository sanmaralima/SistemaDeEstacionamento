package com.locuspark.api.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record PricingConfigurationResponse(
        UUID id,
        UUID companyId,
        Integer dailyTriggerHours,
        BigDecimal dailyValue,
        BigDecimal monthlyBaseValue
) {}