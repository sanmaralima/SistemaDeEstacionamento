package com.locuspark.api.service;

import com.locuspark.api.dto.response.PartnershipResponse;
import com.locuspark.api.entity.Company;
import com.locuspark.api.entity.Partnership;
import com.locuspark.api.enums.DiscountType;
import com.locuspark.api.mapper.PartnershipMapper;
import com.locuspark.api.repository.PartnershipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de Serviço de Parceria - PartnershipService")
class PartnershipServiceTest {

    @Mock
    private PartnershipRepository partnershipRepository;

    @Mock
    private PartnershipMapper partnershipMapper;

    @InjectMocks
    private PartnershipService partnershipService;

    private final UUID companyId = UUID.randomUUID();
    private Company company;

    @BeforeEach
    void setUp() {
        company = Company.builder()
                .id(companyId)
                .name("Estacionamento Central")
                .build();
    }

    @Nested
    @DisplayName("Cenários de Listagem de Parcerias (findAllByCompany)")
    class FindAllByCompany {

        @Test
        @DisplayName("Deve retornar a lista de parcerias da empresa quando existirem cadastros")
        void findAllByCompanySuccess() {
            // Arrange
            Partnership partnership = Partnership.builder()
                    .id(UUID.randomUUID())
                    .company(company)
                    .name("Academia VIP")
                    .discountType(DiscountType.PERCENTAGE)
                    .value(BigDecimal.valueOf(15.00))
                    .build();

            PartnershipResponse response = new PartnershipResponse(
                    partnership.getId(),
                    companyId,
                    "Academia VIP",
                    DiscountType.PERCENTAGE,
                    BigDecimal.valueOf(15.00)
            );

            when(partnershipRepository.findByCompanyId(companyId)).thenReturn(List.of(partnership));
            when(partnershipMapper.toResponse(partnership)).thenReturn(response);

            // Act
            List<PartnershipResponse> result = partnershipService.findAllByCompany(companyId);

            // Assert
            assertThat(result).isNotEmpty();
            assertThat(result).hasSize(1);
            assertThat(result.get(0).companyId()).isEqualTo(companyId);
            assertThat(result.get(0).name()).isEqualTo("Academia VIP");
            assertThat(result.get(0).discountType()).isEqualTo(DiscountType.PERCENTAGE);
            assertThat(result.get(0).value()).isEqualByComparingTo(BigDecimal.valueOf(15.00));

            verify(partnershipRepository).findByCompanyId(companyId);
            verify(partnershipMapper).toResponse(partnership);
        }

        @Test
        @DisplayName("Deve retornar uma lista vazia quando não existirem parcerias para a empresa")
        void findAllByCompanyEmptyList() {
            // Arrange
            when(partnershipRepository.findByCompanyId(companyId)).thenReturn(Collections.emptyList());

            // Act
            List<PartnershipResponse> result = partnershipService.findAllByCompany(companyId);

            // Assert
            assertThat(result).isEmpty();

            verify(partnershipRepository).findByCompanyId(companyId);
            verifyNoInteractions(partnershipMapper);
        }
    }
}
