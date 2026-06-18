package com.locuspark.api.mapper;

import com.locuspark.api.dto.request.VehicleRequest;
import com.locuspark.api.dto.response.VehicleResponse;
import com.locuspark.api.entity.Vehicle;
import com.locuspark.api.types.Plate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VehicleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "client", ignore = true)
    Vehicle toEntity(VehicleRequest request);

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "clientId", source = "client.id")
    VehicleResponse toResponse(Vehicle vehicle);

    default Plate map(String value) {
        return value != null ? new Plate(value) : null;
    }

    default String map(Plate plate) {
        return plate != null ? plate.getValue() : null;
    }
}
