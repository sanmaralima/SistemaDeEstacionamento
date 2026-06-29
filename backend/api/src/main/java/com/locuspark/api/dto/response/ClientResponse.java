package com.locuspark.api.dto.response;

import com.locuspark.api.enums.ClientType;
import java.util.UUID;

public record ClientResponse(
        UUID id,
        String name,
        String cpf,
        String phone,
        ClientType type,
        UUID companyId
) {}
