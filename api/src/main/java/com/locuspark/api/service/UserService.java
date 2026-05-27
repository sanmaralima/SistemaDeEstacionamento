package com.locuspark.api.service;

import com.locuspark.api.dto.request.RegisterRequest;
import com.locuspark.api.dto.request.UserUpdateRequest;
import com.locuspark.api.dto.response.UserResponse;
import com.locuspark.api.entity.Company;
import com.locuspark.api.entity.User;
import com.locuspark.api.enums.UserRole;
import com.locuspark.api.exception.BusinessException;
import com.locuspark.api.mapper.UserMapper;
import com.locuspark.api.repository.UserRepository;
import com.locuspark.api.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse registerUser(RegisterRequest request) {
        // Valida se o username já existe (mantendo a regra do seu AuthController original)
        if (userRepository.findByUsername(request.username()) != null) {
            throw new BusinessException("O usuário '" + request.username() + "' já existe.");
        }

        // Busca a empresa para garantir o vínculo correto do Multi-Tenant
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new BusinessException("Empresa não encontrada com o ID fornecido."));

        // Converte o DTO para Entidade
        User user = userMapper.toEntity(request);

        // Aplica as regras de segurança e de negócio fixas no servidor
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(UserRole.EMPLOYEE); // Padrão imutável pelo frontend: Funcionário
        user.setCompany(company);

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> listAllByCompany(UUID companyId) {
        return userRepository.findAll().stream()
                .filter(u -> u.getCompany() != null && u.getCompany().getId().equals(companyId))
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse updateUser(UUID id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado."));

        userMapper.updateUserFromDto(request, user);

        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        User updated = userRepository.save(user);
        return userMapper.toResponse(updated);
    }

    @Transactional
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado."));
        userRepository.delete(user);
    }
}