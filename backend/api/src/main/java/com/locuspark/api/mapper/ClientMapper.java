package com.locuspark.api.mapper;

import com.locuspark.api.types.Cpf;
import com.locuspark.api.dto.request.ClientRequest;
import com.locuspark.api.dto.response.ClientResponse;
import com.locuspark.api.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    Client toEntity(ClientRequest request);

    @Mapping(target = "companyId", source = "company.id")
    ClientResponse toResponse(Client client);

    default Cpf map(String value) {
        return value != null ? new Cpf(value) : null;
    }

    default String map(Cpf cpf) {
        return cpf != null ? cpf.getValue() : null;
    }
}
