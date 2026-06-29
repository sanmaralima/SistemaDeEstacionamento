package com.locuspark.api.dto.request;

import com.locuspark.api.enums.ClientType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

public record ClientRequest(
        @NotBlank(message = "O nome do cliente é obrigatório")
        String name,

        @NotBlank(message = "O CPF é obrigatório")
        @CPF(message = "CPF inválido")
        String cpf,

        @NotBlank(message = "O telefone é obrigatório")
        String phone,

        @NotNull(message = "O tipo do cliente é obrigatório")
        ClientType type
) {}
