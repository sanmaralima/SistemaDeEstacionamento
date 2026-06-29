package com.locuspark.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.br.CNPJ;

public record CompanyRequest(
        @NotBlank(message = "O nome da empresa é obrigatório")
        String name,

        @NotBlank(message = "O CNPJ é obrigatório")
        @CNPJ(message = "CNPJ inválido")
        String cnpj,

        @NotNull(message = "O total de vagas é obrigatório")
        @PositiveOrZero(message = "O total de vagas não pode ser negativo")
        Integer totalSpots
) {}