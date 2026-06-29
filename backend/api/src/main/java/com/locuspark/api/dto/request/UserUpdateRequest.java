package com.locuspark.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(
        @NotBlank(message = "O nome de usuário é obrigatório")
        String username,

        String password
) {}