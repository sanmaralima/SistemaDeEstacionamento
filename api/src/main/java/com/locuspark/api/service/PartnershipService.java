package com.locuspark.api.service;

import com.locuspark.api.dto.response.PartnershipResponse;
import com.locuspark.api.mapper.PartnershipMapper;
import com.locuspark.api.repository.PartnershipRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PartnershipService {

    private final PartnershipRepository partnershipRepository;
    private final PartnershipMapper partnershipMapper;

    public PartnershipService(PartnershipRepository partnershipRepository, PartnershipMapper partnershipMapper) {
        this.partnershipRepository = partnershipRepository;
        this.partnershipMapper = partnershipMapper;
    }

    @Transactional(readOnly = true)
    public List<PartnershipResponse> findAllByCompany(UUID companyId) {
        return partnershipRepository.findByCompanyId(companyId)
                .stream()
                .map(partnershipMapper::toResponse)
                .collect(Collectors.toList());
    }
}