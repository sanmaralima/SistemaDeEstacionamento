package com.locuspark.api.dto.response;

import com.locuspark.api.enums.CompanyStatus;
import java.util.UUID;

public record CompanyResponse(
        UUID id,
        String name,
        String cnpj,
        Integer totalSpots,
        CompanyStatus status
) {}