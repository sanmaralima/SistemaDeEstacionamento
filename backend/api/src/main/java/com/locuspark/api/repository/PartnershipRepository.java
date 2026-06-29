package com.locuspark.api.repository;

import com.locuspark.api.entity.Partnership;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface PartnershipRepository extends JpaRepository<Partnership, UUID> {
    List<Partnership> findByCompanyId(UUID companyId);
}