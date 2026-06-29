package com.locuspark.api.repository;

import com.locuspark.api.entity.PricingConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface PricingConfigurationRepository extends JpaRepository<PricingConfiguration, UUID> {
    Optional<PricingConfiguration> findByCompanyId(UUID companyId);
}