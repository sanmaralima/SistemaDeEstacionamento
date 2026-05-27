package com.locuspark.api.mapper;

import com.locuspark.api.dto.request.CompanyRequest;
import com.locuspark.api.dto.response.CompanyResponse;
import com.locuspark.api.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompanyMapper {

    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Company toEntity(CompanyRequest request);

    CompanyResponse toResponse(Company company);
}