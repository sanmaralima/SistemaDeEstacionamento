package com.locuspark.api.repository;

import com.locuspark.api.types.Cnpj;
import com.locuspark.api.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {
    boolean existsByCnpj(Cnpj cnpj);
    Optional<Company> findByCnpj(Cnpj cnpj);
}