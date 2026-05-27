package com.locuspark.api.service;

import com.locuspark.api.dto.request.UserUpdateRequest;
import com.locuspark.api.dto.response.UserProfileResponse;
import com.locuspark.api.entity.User;
import com.locuspark.api.exception.UserNotFoundException;
import com.locuspark.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserProfileResponse updateUser(UUID id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));

        user.setUsername(request.username());

        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        User updatedUser = userRepository.save(user);

        return new UserProfileResponse(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getRole());
    }

    @Transactional
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));

        userRepository.delete(user);
    }
}