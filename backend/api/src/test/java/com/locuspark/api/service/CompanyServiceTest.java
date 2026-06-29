package com.locuspark.api.service;

import com.locuspark.api.types.Cnpj;
import com.locuspark.api.dto.request.CompanyRequest;
import com.locuspark.api.dto.response.CompanyResponse;
import com.locuspark.api.entity.Company;
import com.locuspark.api.enums.CompanyStatus;
import com.locuspark.api.exception.BusinessException;
import com.locuspark.api.mapper.CompanyMapper;
import com.locuspark.api.repository.CompanyRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyRepository repository;

    @Mock
    private CompanyMapper mapper;

    @InjectMocks
    private CompanyService service;

    private final UUID id = UUID.randomUUID();
    private final CompanyRequest request = new CompanyRequest("Locus Park", "06990590000123", 10);
    private final Company company = Company.builder().id(id).name("Locus Park").cnpj(new Cnpj("06990590000123")).totalSpots(10).status(CompanyStatus.ACTIVE).build();
    private final CompanyResponse response = new CompanyResponse(id, "Locus Park", "06990590000123", 10, CompanyStatus.ACTIVE);

    @Nested
    @DisplayName("Criação de Empresas")
    class Create {
        @Test
        @DisplayName("Deve salvar e retornar a empresa cadastrada com sucesso")
        void createSuccess() {
            when(repository.existsByCnpj(any(Cnpj.class))).thenReturn(false);
            when(mapper.toEntity(request)).thenReturn(company);
            when(repository.save(company)).thenReturn(company);
            when(mapper.toResponse(company)).thenReturn(response);

            CompanyResponse result = service.createCompany(request);
            assertNotNull(result);
            assertEquals(response, result);
        }

        @Test
        @DisplayName("Deve lançar BusinessException se o CNPJ já existir")
        void createFailCnpjExists() {
            when(repository.existsByCnpj(any(Cnpj.class))).thenReturn(true);
            assertThrows(BusinessException.class, () -> service.createCompany(request));
        }

        @Test
        @DisplayName("Deve lançar BusinessException se o total de vagas for menor ou igual a zero")
        void createFailSpotsZero() {
            CompanyRequest invalid = new CompanyRequest("Locus Park", "12345678000100", 0);
            assertThrows(BusinessException.class, () -> service.createCompany(invalid));
        }
    }

    @Nested
    @DisplayName("Busca de Empresas")
    class Get {
        @Test
        @DisplayName("Deve retornar CompanyResponse para ID existente")
        void getSuccess() {
            when(repository.findById(id)).thenReturn(Optional.of(company));
            when(mapper.toResponse(company)).thenReturn(response);

            CompanyResponse result = service.getCompanyById(id);
            assertNotNull(result);
            assertEquals(response, result);
        }

        @Test
        @DisplayName("Deve lançar BusinessException se o ID não for encontrado")
        void getFailNotFound() {
            when(repository.findById(id)).thenReturn(Optional.empty());
            assertThrows(BusinessException.class, () -> service.getCompanyById(id));
        }
    }

    @Nested
    @DisplayName("Atualização de Empresas")
    class Update {
        @Test
        @DisplayName("Deve atualizar dados da empresa com sucesso")
        void updateSuccess() {
            when(repository.findById(id)).thenReturn(Optional.of(company));
            when(repository.findAll()).thenReturn(List.of(company));
            when(repository.save(company)).thenReturn(company);
            when(mapper.toResponse(company)).thenReturn(response);

            CompanyResponse result = service.updateCompany(id, request);
            assertNotNull(result);
            assertEquals(response, result);
        }

        @Test
        @DisplayName("Deve lançar BusinessException se o CNPJ pertencer a outra empresa")
        void updateFailCnpjAnotherCompany() {
            UUID targetId = UUID.randomUUID();
            Company targetCompany = Company.builder().id(targetId).name("Target").cnpj(new Cnpj("00394460005887")).totalSpots(10).status(CompanyStatus.ACTIVE).build();
            Company otherCompany = Company.builder().id(UUID.randomUUID()).name("Other").cnpj(new Cnpj("06990590000123")).totalSpots(10).status(CompanyStatus.ACTIVE).build();
            CompanyRequest updateRequest = new CompanyRequest("Target Updated", "06990590000123", 10);

            when(repository.findById(targetId)).thenReturn(Optional.of(targetCompany));
            when(repository.findAll()).thenReturn(List.of(targetCompany, otherCompany));

            assertThrows(BusinessException.class, () -> service.updateCompany(targetId, updateRequest));
        }

        @Test
        @DisplayName("Deve lançar BusinessException se totalSpots for menor ou igual a zero na atualização")
        void updateFailSpotsZero() {
            CompanyRequest invalid = new CompanyRequest("Locus Park", "06990590000123", 0);
            when(repository.findById(id)).thenReturn(Optional.of(company));

            assertThrows(BusinessException.class, () -> service.updateCompany(id, invalid));
        }
    }

    @Nested
    @DisplayName("Exclusão de Empresas")
    class Delete {
        @Test
        @DisplayName("Deve inativar a empresa com sucesso")
        void deleteSuccess() {
            when(repository.findById(id)).thenReturn(Optional.of(company));
            service.deleteCompany(id);
            assertEquals(CompanyStatus.INACTIVE, company.getStatus());
            verify(repository).save(company);
        }

        @Test
        @DisplayName("Deve lançar BusinessException se a empresa já estiver inativa")
        void deleteFailAlreadyInactive() {
            company.setStatus(CompanyStatus.INACTIVE);
            when(repository.findById(id)).thenReturn(Optional.of(company));

            assertThrows(BusinessException.class, () -> service.deleteCompany(id));
        }
    }
}
