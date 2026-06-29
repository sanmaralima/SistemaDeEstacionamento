package com.locuspark.api.dto.request;

import com.locuspark.api.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record VehicleRequest(
        @NotBlank(message = "A placa do veículo é obrigatória")
        String plate,

        @NotBlank(message = "O modelo do veículo é obrigatório")
        String model,

        @NotBlank(message = "A cor do veículo é obrigatória")
        String color,

        @NotBlank(message = "O tipo do veículo é obrigatório")
        VehicleType type,

        UUID clientId
) {}
