package com.locuspark.api.controller;

import com.locuspark.api.dto.request.PricingConfigurationRequest;
import com.locuspark.api.dto.response.PricingConfigurationResponse;
import com.locuspark.api.service.ConfigurationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/configurations/pricing")
public class PricingConfigurationController {

    private final ConfigurationService configurationService;

    public PricingConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @GetMapping
    public ResponseEntity<PricingConfigurationResponse> getPricing(@RequestAttribute("companyId") UUID companyId) {
        PricingConfigurationResponse response = configurationService.getPricingByCompany(companyId);
        return ResponseEntity.ok(response);
    }

    // Salva ou atualiza os preços do pátio
    @PutMapping
    public ResponseEntity<PricingConfigurationResponse> updatePricing(
            @RequestAttribute("companyId") UUID companyId,
            @RequestBody @Valid PricingConfigurationRequest request) {
        return ResponseEntity.ok(configurationService.saveOrUpdatePricing(companyId, request));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePricing(@RequestAttribute("companyId") UUID companyId) {
        configurationService.deletePricing(companyId);
        return ResponseEntity.noContent().build();
    }
}