package com.locuspark.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record VehicleRequest(
        @NotBlank(message = "A placa do veículo é obrigatória")
        String plate,

        @NotBlank(message = "O modelo do veículo é obrigatório")
        String model,

        @NotBlank(message = "A cor do veículo é obrigatória")
        String color,

        UUID clientId
) {}
