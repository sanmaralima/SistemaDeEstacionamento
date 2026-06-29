package com.locuspark.api.mapper;

import com.locuspark.api.entity.TariffConfiguration;
import com.locuspark.api.dto.response.TariffConfigurationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TariffConfigurationMapper {
    @Mapping(source = "company.id", target = "companyId")
    TariffConfigurationResponse toResponse(TariffConfiguration entity);
}