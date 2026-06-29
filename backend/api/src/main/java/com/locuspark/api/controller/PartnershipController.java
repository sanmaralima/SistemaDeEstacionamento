package com.locuspark.api.controller;

import com.locuspark.api.dto.request.PartnershipRequest;
import com.locuspark.api.dto.response.PartnershipResponse;
import com.locuspark.api.service.PartnershipService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<PartnershipResponse> getById(
            @PathVariable UUID id,
            @RequestAttribute("companyId") UUID companyId) {
        return ResponseEntity.ok(partnershipService.findByIdAndCompany(id, companyId));
    }

    @PostMapping
    public ResponseEntity<PartnershipResponse> create(
            @RequestAttribute("companyId") UUID companyId,
            @RequestBody @Valid PartnershipRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(partnershipService.createPartnership(companyId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartnershipResponse> update(
            @PathVariable UUID id,
            @RequestAttribute("companyId") UUID companyId,
            @RequestBody @Valid PartnershipRequest request) {
        return ResponseEntity.ok(partnershipService.updatePartnership(id, companyId, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @RequestAttribute("companyId") UUID companyId) {
        partnershipService.deletePartnership(id, companyId);
        return ResponseEntity.noContent().build();
    }
}