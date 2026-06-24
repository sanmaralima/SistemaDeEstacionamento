package com.locuspark.api.controller;

import com.locuspark.api.dto.response.PartnershipResponse;
import com.locuspark.api.enums.DiscountType;
import com.locuspark.api.repository.UserRepository;
import com.locuspark.api.security.TokenService;
import com.locuspark.api.service.PartnershipService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PartnershipController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Testes de Controlador de Parceria - PartnershipController")
class PartnershipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PartnershipService partnershipService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    private final UUID companyId = UUID.randomUUID();

    @Nested
    @DisplayName("Cenários de Listagem de Parcerias")
    class ListPartnerships {

        @Test
        @DisplayName("GET /api/partnerships - Deve retornar HTTP 200 OK com a lista de parcerias da empresa")
        void listAllSuccess() throws Exception {
            // Arrange
            UUID partnershipId = UUID.randomUUID();
            PartnershipResponse response = new PartnershipResponse(
                    partnershipId,
                    companyId,
                    "Academia VIP",
                    DiscountType.PERCENTAGE,
                    BigDecimal.valueOf(15.00)
            );

            when(partnershipService.findAllByCompany(companyId)).thenReturn(List.of(response));

            // Act & Assert
            mockMvc.perform(get("/partnerships")
                            .requestAttr("companyId", companyId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].id").value(partnershipId.toString()))
                    .andExpect(jsonPath("$[0].companyId").value(companyId.toString()))
                    .andExpect(jsonPath("$[0].name").value("Academia VIP"))
                    .andExpect(jsonPath("$[0].discountType").value("PERCENTAGE"))
                    .andExpect(jsonPath("$[0].value").value(15.00));

            verify(partnershipService).findAllByCompany(companyId);
        }
    }
}
