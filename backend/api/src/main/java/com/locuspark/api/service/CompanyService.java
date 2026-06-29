package com.locuspark.api.service;

import com.locuspark.api.types.Cnpj;
import com.locuspark.api.dto.request.CompanyRequest;
import com.locuspark.api.dto.response.CompanyResponse;
import com.locuspark.api.entity.Company;
import com.locuspark.api.enums.CompanyStatus;
import com.locuspark.api.exception.BusinessException;
import com.locuspark.api.mapper.CompanyMapper;
import com.locuspark.api.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Transactional
    public CompanyResponse createCompany(CompanyRequest request) {
        // Validação de duplicidade usando o método do repository existente
        if (companyRepository.existsByCnpj(new Cnpj(request.cnpj()))) {
            throw new BusinessException("Já existe uma empresa cadastrada com este CNPJ.");
        }

        if (request.totalSpots() <= 0) {
            throw new BusinessException("O número total de vagas deve ser maior que zero.");
        }

        // Mapeia DTO para Entidade (O MapStruct já fixa o status como ACTIVE conforme a interface)
        Company company = companyMapper.toEntity(request);
        Company savedCompany = companyRepository.save(company);

        return companyMapper.toResponse(savedCompany);
    }

    @Transactional(readOnly = true)
    public List<CompanyResponse> listAllCompanies() {
        return companyRepository.findAll().stream()
                .map(companyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CompanyResponse getCompanyById(UUID id) {
        Company company = findByIdOrThrow(id);
        return companyMapper.toResponse(company);
    }

    @Transactional
    public CompanyResponse updateCompany(UUID id, CompanyRequest request) {
        Company company = findByIdOrThrow(id);

        // Validação complexa de CNPJ: Se o CNPJ enviado já existir em OUTRA empresa, lança erro
        // Para isso funcionar perfeitamente, adicione o método Optional<Company> findByCnpj(String cnpj) no seu CompanyRepository
        Cnpj requestCnpj = new Cnpj(request.cnpj());
        companyRepository.findAll().stream()
                .filter(c -> c.getCnpj().equals(requestCnpj) && !c.getId().equals(id))
                .findFirst()
                .ifPresent(c -> {
                    throw new BusinessException("Este CNPJ já está sendo utilizado por outra empresa.");
                });

        if (request.totalSpots() <= 0) {
            throw new BusinessException("O número total de vagas deve ser maior que zero.");
        }

        // Atualiza os dados da entidade existente utilizando os dados do Request
        company.setName(request.name());
        company.setCnpj(new Cnpj(request.cnpj()));
        company.setTotalSpots(request.totalSpots());

        Company updatedCompany = companyRepository.save(company);
        return companyMapper.toResponse(updatedCompany);
    }

    @Transactional
    public void deleteCompany(UUID id) {
        Company company = findByIdOrThrow(id);

        // Soft Delete (Inativação lógica da empresa) para manter a integridade dos dados históricos no SaaS
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
                .orElseThrow(() -> new BusinessException("Empresa não encontrada com o ID fornecido."));
    }
}