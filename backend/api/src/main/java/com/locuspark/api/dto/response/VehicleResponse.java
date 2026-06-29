package com.locuspark.api.dto.response;

import com.locuspark.api.enums.VehicleType;

import java.util.UUID;

public record VehicleResponse(
        UUID id,
        String plate,
        String model,
        String color,
        VehicleType type,
        UUID clientId,
        UUID companyId
) {}
