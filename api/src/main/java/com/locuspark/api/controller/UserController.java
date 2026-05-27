package com.locuspark.api.controller;

import com.locuspark.api.dto.request.UserUpdateRequest;
import com.locuspark.api.dto.response.UserResponse;
import com.locuspark.api.entity.User;
import com.locuspark.api.exception.BusinessException;
import com.locuspark.api.mapper.UserMapper;
import com.locuspark.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(@AuthenticationPrincipal User user) {
        if (user == null) {
            throw new BusinessException("Usuário não encontrado no sistema.");
        }
        // Retorna o DTO completo, incluindo o companyId mapeado pelo MapStruct
        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<UserResponse>> getByCompany(@PathVariable UUID companyId) {
        return ResponseEntity.ok(userService.listAllByCompany(companyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable UUID id, @RequestBody @Valid UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}