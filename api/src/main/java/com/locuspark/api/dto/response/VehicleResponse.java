package com.locuspark.api.dto.response;

import java.util.UUID;

public record VehicleResponse(
        UUID id,
        String plate,
        String model,
        String color,
        UUID clientId,
        UUID companyId
) {}
