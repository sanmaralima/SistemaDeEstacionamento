package com.locuspark.api.service;

import com.locuspark.api.dto.request.VehicleRequest;
import com.locuspark.api.dto.response.VehicleResponse;
import com.locuspark.api.entity.Client;
import com.locuspark.api.entity.Company;
import com.locuspark.api.entity.Vehicle;
import com.locuspark.api.exception.BusinessException;
import com.locuspark.api.mapper.VehicleMapper;
import com.locuspark.api.repository.ClientRepository;
import com.locuspark.api.repository.CompanyRepository;
import com.locuspark.api.repository.VehicleRepository;
import com.locuspark.api.types.Plate;
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
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new BusinessException("Empresa não encontrada."));

        Plate plate = new Plate(request.plate());
        if (vehicleRepository.existsByPlateAndCompanyId(plate, companyId)) {
            throw new BusinessException("Já existe um veículo cadastrado com esta placa nesta empresa.");
        }

        Client client = null;
        if (request.clientId() != null) {
            client = clientRepository.findByIdAndCompanyId(request.clientId(), companyId)
                    .orElseThrow(() -> new BusinessException("Cliente não encontrado ou não pertence a esta empresa."));
        }

        Vehicle vehicle = vehicleMapper.toEntity(request);
        vehicle.setCompany(company);
        vehicle.setClient(client);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toResponse(savedVehicle);
    }

    public List<VehicleResponse> listAllVehiclesByCompany(UUID companyId) {
        List<Vehicle> vehicles = vehicleRepository.findByCompanyId(companyId);
        return vehicles.stream()
                .map(vehicleMapper::toResponse)
                .toList();
    }

    public VehicleResponse getVehicleByIdAndCompany(UUID id, UUID companyId) {
        Vehicle vehicle = vehicleRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new BusinessException("Veículo não encontrado ou não pertence a esta empresa."));
        return vehicleMapper.toResponse(vehicle);
    }
}
