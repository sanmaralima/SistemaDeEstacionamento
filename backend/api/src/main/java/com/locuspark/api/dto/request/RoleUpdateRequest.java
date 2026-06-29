package com.locuspark.api.dto.request;

import com.locuspark.api.enums.UserRole;
import jakarta.validation.constraints.NotNull;

public record RoleUpdateRequest(
        @NotNull(message = "A nova role é obrigatória")
        UserRole role
) {}
