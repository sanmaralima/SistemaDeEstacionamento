package com.locuspark.api.dto.response;

import com.locuspark.api.enums.UserRole;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        UserRole role,
        UUID companyId
) {}