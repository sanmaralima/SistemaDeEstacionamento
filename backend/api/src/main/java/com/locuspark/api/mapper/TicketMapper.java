package com.locuspark.api.mapper;

import com.locuspark.api.dto.response.TicketResponse;
import com.locuspark.api.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { VehicleMapper.class })
public interface TicketMapper {

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "partnership.id", target = "partnershipId")
    TicketResponse toResponse(Ticket ticket);
}