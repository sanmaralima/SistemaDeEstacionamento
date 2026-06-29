package com.locuspark.api.dto.response;

import com.locuspark.api.enums.DiscountType;
import java.math.BigDecimal;
import java.util.UUID;

public record PartnershipResponse(
        UUID id,
        UUID companyId,
        String name,
        DiscountType discountType,
        BigDecimal value
) {}