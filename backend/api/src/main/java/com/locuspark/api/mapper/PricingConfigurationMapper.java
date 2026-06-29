package com.locuspark.api.mapper;

import com.locuspark.api.entity.PricingConfiguration;
import com.locuspark.api.dto.response.PricingConfigurationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PricingConfigurationMapper {
    @Mapping(source = "company.id", target = "companyId")
    PricingConfigurationResponse toResponse(PricingConfiguration entity);
}