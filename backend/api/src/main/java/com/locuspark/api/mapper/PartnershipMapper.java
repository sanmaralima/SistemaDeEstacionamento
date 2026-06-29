package com.locuspark.api.mapper;

import com.locuspark.api.entity.Partnership;
import com.locuspark.api.dto.request.PartnershipRequest;
import com.locuspark.api.dto.response.PartnershipResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PartnershipMapper {

    @Mapping(source = "company.id", target = "companyId")
    PartnershipResponse toResponse(Partnership entity);

    // Converte o Request em Entidade para o método de criação (POST)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    Partnership toEntity(PartnershipRequest request);

    // Atualiza a entidade existente com os dados do DTO para o método de edição (PUT)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    void updateFromDto(PartnershipRequest request, @MappingTarget Partnership entity);
}