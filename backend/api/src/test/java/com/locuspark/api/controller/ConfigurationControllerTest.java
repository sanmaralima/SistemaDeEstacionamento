package com.locuspark.api.controller;

import com.locuspark.api.dto.response.PricingConfigurationResponse;
import com.locuspark.api.dto.response.TariffConfigurationResponse;
import com.locuspark.api.repository.UserRepository;
import com.locuspark.api.security.TokenService;
import com.locuspark.api.service.ConfigurationService;
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
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ConfigurationController.class, PricingConfigurationController.class})
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Testes de Controlador de Configuração - ConfigurationController")
class ConfigurationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConfigurationService configurationService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    private final UUID companyId = UUID.randomUUID();

    @Nested
    @DisplayName("Cenários de Busca de Configurações")
    class GetConfigurations {

        @Test
        @DisplayName("GET /api/configurations/tariff - Deve retornar HTTP 200 OK com os dados de tarifa da empresa")
        void getTariffSuccess() throws Exception {
            // Arrange
            UUID tariffId = UUID.randomUUID();
            TariffConfigurationResponse response = new TariffConfigurationResponse(
                    tariffId,
                    companyId,
                    15,
                    BigDecimal.valueOf(10.00),
                    BigDecimal.valueOf(2.50),
                    BigDecimal.valueOf(50.00),
                    BigDecimal.valueOf(20.00)
            );

            when(configurationService.getTariffByCompany(companyId)).thenReturn(response);

            // Act & Assert
            mockMvc.perform(get("/configurations/tariff")
                            .requestAttr("companyId", companyId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(tariffId.toString()))
                    .andExpect(jsonPath("$.companyId").value(companyId.toString()))
                    .andExpect(jsonPath("$.toleranceMinutes").value(15))
                    .andExpect(jsonPath("$.firstHourValue").value(10.00))
                    .andExpect(jsonPath("$.additionalFractionValue").value(2.50))
                    .andExpect(jsonPath("$.overnightFee").value(50.00))
                    .andExpect(jsonPath("$.lostTicketFee").value(20.00));

            verify(configurationService).getTariffByCompany(companyId);
        }

        @Test
        @DisplayName("GET /api/configurations/pricing - Deve retornar HTTP 200 OK com os dados de precificação da empresa")
        void getPricingSuccess() throws Exception {
            // Arrange
            UUID pricingId = UUID.randomUUID();
            PricingConfigurationResponse response = new PricingConfigurationResponse(
                    pricingId,
                    companyId,
                    6,
                    BigDecimal.valueOf(40.00),
                    BigDecimal.valueOf(200.00)
            );

            when(configurationService.getPricingByCompany(companyId)).thenReturn(response);

            // Act & Assert
            mockMvc.perform(get("/configurations/pricing")
                            .requestAttr("companyId", companyId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(pricingId.toString()))
                    .andExpect(jsonPath("$.companyId").value(companyId.toString()))
                    .andExpect(jsonPath("$.dailyTriggerHours").value(6))
                    .andExpect(jsonPath("$.dailyValue").value(40.00))
                    .andExpect(jsonPath("$.monthlyBaseValue").value(200.00));

            verify(configurationService).getPricingByCompany(companyId);
        }
    }
}
