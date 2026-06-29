package com.locuspark.api.service;

import com.locuspark.api.types.Cpf;
import com.locuspark.api.dto.request.ClientRequest;
import com.locuspark.api.dto.response.ClientResponse;
import com.locuspark.api.entity.Client;
import com.locuspark.api.entity.Company;
import com.locuspark.api.exception.BusinessException;
import com.locuspark.api.mapper.ClientMapper;
import com.locuspark.api.repository.ClientRepository;
import com.locuspark.api.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientService {

    private final ClientRepository clientRepository;
    private final CompanyRepository companyRepository;
    private final ClientMapper clientMapper;

    @Transactional
    public ClientResponse createClient(UUID companyId, ClientRequest request) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new BusinessException("Empresa não encontrada."));

        if (clientRepository.existsByCpfAndCompanyId(new Cpf(request.cpf()), companyId)) {
            throw new BusinessException("Já existe um cliente cadastrado com este CPF nesta empresa.");
        }

        Client client = clientMapper.toEntity(request);
        client.setCompany(company);

        Client savedClient = clientRepository.save(client);
        return clientMapper.toResponse(savedClient);
    }

    public List<ClientResponse> listAllClientsByCompany(UUID companyId) {
        List<Client> clients = clientRepository.findByCompanyId(companyId);
        return clients.stream()
                .map(clientMapper::toResponse)
                .toList();
    }

    public ClientResponse getClientByIdAndCompany(UUID id, UUID companyId) {
        Client client = clientRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado ou não pertence a esta empresa."));
        return clientMapper.toResponse(client);
    }

    @Transactional
    public ClientResponse updateClient(UUID id, UUID companyId, ClientRequest request) {
        Client client = clientRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado ou não pertence a esta empresa."));

        if (!client.getCpf().getValue().equals(request.cpf()) && clientRepository.existsByCpfAndCompanyId(new Cpf(request.cpf()), companyId)) {
            throw new BusinessException("Já existe outro cliente cadastrado com este CPF nesta empresa.");
        }

        client.setName(request.name());
        client.setPhone(request.phone());
        client.setType(request.type());
        client.setCpf(new Cpf(request.cpf()));

        Client updatedClient = clientRepository.save(client);
        return clientMapper.toResponse(updatedClient);
    }

    @Transactional
    public void deleteClient(UUID id, UUID companyId) {
        Client client = clientRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado ou não pertence a esta empresa."));
        clientRepository.delete(client);
    }
}

