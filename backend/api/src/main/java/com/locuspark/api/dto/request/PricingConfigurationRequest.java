package com.locuspark.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record PricingConfigurationRequest(
        @NotNull(message = "A quantidade de horas para gatilho de diária é obrigatória")
        @PositiveOrZero
        Integer dailyTriggerHours,

        @NotNull(message = "O valor da diária é obrigatório")
        @PositiveOrZero
        BigDecimal dailyValue,

        @NotNull(message = "O valor base mensal é obrigatório")
        @PositiveOrZero
        BigDecimal monthlyBaseValue
) {}