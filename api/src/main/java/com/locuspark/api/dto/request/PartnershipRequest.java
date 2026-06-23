package com.locuspark.api.dto.request;

import com.locuspark.api.enums.DiscountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record PartnershipRequest(
        @NotBlank(message = "O nome do convênio é obrigatório")
        String name,

        @NotNull(message = "O tipo de desconto é obrigatório")
        DiscountType discountType,

        @NotNull(message = "O valor do desconto é obrigatório")
        @PositiveOrZero(message = "O valor do desconto não pode ser negativo")
        BigDecimal value
) {}