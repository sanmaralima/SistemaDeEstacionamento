package com.locuspark.api.repository;

import com.locuspark.api.entity.TariffConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface TariffConfigurationRepository extends JpaRepository<TariffConfiguration, UUID> {
    Optional<TariffConfiguration> findByCompanyId(UUID companyId);
}