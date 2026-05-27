package com.locuspark.api.controller;

import com.locuspark.api.dto.request.UserUpdateRequest;
import com.locuspark.api.dto.response.UserProfileResponse;
import com.locuspark.api.entity.User;
import com.locuspark.api.exception.UserNotFoundException;
import com.locuspark.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(@AuthenticationPrincipal User user) {

        if (user == null) {
            throw new UserNotFoundException("Usuário não encontrado no sistema.");
        }
        UserProfileResponse profile = new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );

        return ResponseEntity.ok(profile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileResponse> update(
            @PathVariable UUID id,
            @RequestBody @Valid UserUpdateRequest request) {
        UserProfileResponse updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}