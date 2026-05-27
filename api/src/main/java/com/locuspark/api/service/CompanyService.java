package com.locuspark.api.service;

import com.locuspark.api.dto.request.CompanyCreateRequest;
import com.locuspark.api.entity.Company;
import com.locuspark.api.enums.CompanyStatus;
import com.locuspark.api.exception.BusinessException;
import com.locuspark.api.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional
    public Company createCompany(CompanyCreateRequest request) {
        if (companyRepository.existsByCnpj(request.getCnpj())) {
            throw new BusinessException("Já existe uma empresa cadastrada com este CNPJ.");
        }

        if (request.getTotalSpots() <= 0) {
            throw new BusinessException("O número total de vagas deve ser maior que zero.");
        }

        Company company = Company.builder()
                .name(request.getName())
                .cnpj(request.getCnpj())
                .totalSpots(request.getTotalSpots())
                .status(CompanyStatus.ACTIVE)
                .build();

        return companyRepository.save(company);
    }

    @Transactional
    public void inactivateCompany(UUID companyId) {
        Company company = findByIdOrThrow(companyId);

        if (company.getStatus() == CompanyStatus.INACTIVE) {
            throw new BusinessException("A empresa já está inativa.");
        }

        company.setStatus(CompanyStatus.INACTIVE);
        companyRepository.save(company);
    }

    public boolean hasAvailableSpots(UUID companyId, int currentPendingTickets) {
        Company company = findByIdOrThrow(companyId);

        if (company.getStatus() == CompanyStatus.INACTIVE) {
            return false;
        }

        return currentPendingTickets < company.getTotalSpots();
    }

    public Company findByIdOrThrow(UUID id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Empresa não encontrada."));
    }
}