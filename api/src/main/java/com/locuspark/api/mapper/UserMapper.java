package com.locuspark.api.mapper;

import com.locuspark.api.dto.request.RegisterRequest;
import com.locuspark.api.dto.request.UserUpdateRequest;
import com.locuspark.api.dto.response.UserResponse;
import com.locuspark.api.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true) // Criptografada no Service
    @Mapping(target = "role", ignore = true)     // Forçada como EMPLOYEE no Service
    @Mapping(target = "company", ignore = true)  // Buscada no banco pelo Service
    User toEntity(RegisterRequest request);

    @Mapping(target = "companyId", source = "company.id")
    UserResponse toResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "company", ignore = true)
    void updateUserFromDto(UserUpdateRequest request, @MappingTarget User user);
}