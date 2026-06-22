package com.locuspark.api.controller;

import com.locuspark.api.dto.response.PartnershipResponse;
import com.locuspark.api.service.PartnershipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/partnerships")
public class PartnershipController {

    private final PartnershipService partnershipService;

    public PartnershipController(PartnershipService partnershipService) {
        this.partnershipService = partnershipService;
    }

    @GetMapping
    public ResponseEntity<List<PartnershipResponse>> listAll(@RequestAttribute("companyId") UUID companyId) {
        List<PartnershipResponse> response = partnershipService.findAllByCompany(companyId);
        return ResponseEntity.ok(response);
    }
}