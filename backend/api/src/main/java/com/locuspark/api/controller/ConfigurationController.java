package com.locuspark.api.controller;

import com.locuspark.api.dto.request.TariffConfigurationRequest;
import com.locuspark.api.dto.response.TariffConfigurationResponse;
import com.locuspark.api.service.ConfigurationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/configurations/tariff")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @GetMapping
    public ResponseEntity<TariffConfigurationResponse> getTariff(@RequestAttribute("companyId") UUID companyId) {
        TariffConfigurationResponse response = configurationService.getTariffByCompany(companyId);
        return ResponseEntity.ok(response);
    }

    // Salva ou atualiza a tarifa do pátio
    @PutMapping
    public ResponseEntity<TariffConfigurationResponse> updateTariff(
            @RequestAttribute("companyId") UUID companyId,
            @RequestBody @Valid TariffConfigurationRequest request) {
        return ResponseEntity.ok(configurationService.saveOrUpdateTariff(companyId, request));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTariff(@RequestAttribute("companyId") UUID companyId) {
        configurationService.deleteTariff(companyId);
        return ResponseEntity.noContent().build();
    }
}