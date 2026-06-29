package com.locuspark.api.controller;

import com.locuspark.api.dto.request.VehicleRequest;
import com.locuspark.api.dto.response.VehicleResponse;
import com.locuspark.api.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/companies/{companyId}/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleResponse> create(@PathVariable UUID companyId, @RequestBody VehicleRequest request) {
        return ResponseEntity.ok(vehicleService.createVehicle(companyId, request));
    }

    @GetMapping
    public ResponseEntity<List<VehicleResponse>> listAll(@PathVariable UUID companyId) {
        return ResponseEntity.ok(vehicleService.listAllVehiclesByCompany(companyId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponse> getById(@PathVariable UUID companyId, @PathVariable UUID id) {
        return ResponseEntity.ok(vehicleService.getVehicleByIdAndCompany(id, companyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponse> update(@PathVariable UUID companyId, @PathVariable UUID id, @RequestBody VehicleRequest request) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, companyId, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID companyId, @PathVariable UUID id) {
        vehicleService.deleteVehicle(id, companyId);
        return ResponseEntity.noContent().build();
    }
}