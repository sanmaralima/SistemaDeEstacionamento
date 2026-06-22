package com.locuspark.api.controller;

import com.locuspark.api.dto.response.PricingConfigurationResponse;
import com.locuspark.api.dto.response.TariffConfigurationResponse;
import com.locuspark.api.service.ConfigurationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/configurations")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @GetMapping("/tariff")
    public ResponseEntity<TariffConfigurationResponse> getTariff(@RequestAttribute("companyId") UUID companyId) {
        TariffConfigurationResponse response = configurationService.getTariffByCompany(companyId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pricing")
    public ResponseEntity<PricingConfigurationResponse> getPricing(@RequestAttribute("companyId") UUID companyId) {
        PricingConfigurationResponse response = configurationService.getPricingByCompany(companyId);
        return ResponseEntity.ok(response);
    }
}