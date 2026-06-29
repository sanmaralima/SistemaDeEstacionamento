package com.locuspark.api.dto.request;

import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record TariffConfigurationRequest(
        @PositiveOrZero(message = "Os minutos de tolerância não podem ser negativos")
        Integer toleranceMinutes,

        @PositiveOrZero(message = "O valor da primeira hora não pode ser negativo")
        BigDecimal firstHourValue,

        @PositiveOrZero(message = "O valor da fração adicional não pode ser negativo")
        BigDecimal additionalFractionValue,

        @PositiveOrZero(message = "A taxa de pernoite não pode ser negativa")
        BigDecimal overnightFee,

        @PositiveOrZero(message = "A taxa de perda de ticket não pode ser negativa")
        BigDecimal lostTicketFee
) {}