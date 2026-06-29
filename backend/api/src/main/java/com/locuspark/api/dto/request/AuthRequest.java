package com.locuspark.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
    @NotBlank(message = "O nome de usuário é obrigatório")
    String username,

    @NotBlank(message = "A senha é obrigatória")
    String password
) {}