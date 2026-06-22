package com.locuspark.api.service;

import com.locuspark.api.dto.response.PricingConfigurationResponse;
import com.locuspark.api.dto.response.TariffConfigurationResponse;
import com.locuspark.api.exception.ResourceNotFoundException;
import com.locuspark.api.mapper.PricingConfigurationMapper;
import com.locuspark.api.mapper.TariffConfigurationMapper;
import com.locuspark.api.repository.PricingConfigurationRepository;
import com.locuspark.api.repository.TariffConfigurationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ConfigurationService {

    private final TariffConfigurationRepository tariffRepository;
    private final PricingConfigurationRepository pricingRepository;
    private final TariffConfigurationMapper tariffMapper;
    private final PricingConfigurationMapper pricingMapper;

    public ConfigurationService(TariffConfigurationRepository tariffRepository,
                                PricingConfigurationRepository pricingRepository,
                                TariffConfigurationMapper tariffMapper,
                                PricingConfigurationMapper pricingMapper) {
        this.tariffRepository = tariffRepository;
        this.pricingRepository = pricingRepository;
        this.tariffMapper = tariffMapper;
        this.pricingMapper = pricingMapper;
    }

    @Transactional(readOnly = true)
    public TariffConfigurationResponse getTariffByCompany(UUID companyId) {
        return tariffRepository.findByCompanyId(companyId)
                .map(tariffMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Configuração tarifária não encontrada para esta empresa."));
    }

    @Transactional(readOnly = true)
    public PricingConfigurationResponse getPricingByCompany(UUID companyId) {
        return pricingRepository.findByCompanyId(companyId)
                .map(pricingMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Configuração de preços não encontrada para esta empresa."));
    }
}