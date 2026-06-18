package com.locuspark.api.service;

import com.locuspark.api.dto.request.VehicleRequest;
import com.locuspark.api.dto.response.VehicleResponse;
import com.locuspark.api.mapper.VehicleMapper;
import com.locuspark.api.repository.ClientRepository;
import com.locuspark.api.repository.CompanyRepository;
import com.locuspark.api.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final CompanyRepository companyRepository;
    private final ClientRepository clientRepository;
    private final VehicleMapper vehicleMapper;

    @Transactional
    public VehicleResponse createVehicle(UUID companyId, VehicleRequest request) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<VehicleResponse> listAllVehiclesByCompany(UUID companyId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public VehicleResponse getVehicleByIdAndCompany(UUID id, UUID companyId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
