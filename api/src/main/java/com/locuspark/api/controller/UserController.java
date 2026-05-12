package com.locuspark.api.controller;

import com.locuspark.api.dto.response.UserProfileResponse;
import com.locuspark.api.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(@AuthenticationPrincipal User user) {
        UserProfileResponse profile = new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );

        return ResponseEntity.ok(profile);
    }
}