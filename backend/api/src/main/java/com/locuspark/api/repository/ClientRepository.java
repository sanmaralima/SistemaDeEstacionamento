package com.locuspark.api.repository;

import com.locuspark.api.types.Cpf;
import com.locuspark.api.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    boolean existsByCpfAndCompanyId(Cpf cpf, UUID companyId);
    List<Client> findByCompanyId(UUID companyId);
    Optional<Client> findByIdAndCompanyId(UUID id, UUID companyId);
}
