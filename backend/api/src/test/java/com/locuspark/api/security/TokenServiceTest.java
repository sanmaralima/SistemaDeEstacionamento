package com.locuspark.api.security;

import com.locuspark.api.entity.User;
import com.locuspark.api.enums.UserRole;
import com.locuspark.api.exception.TokenInvalidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    private User user;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(tokenService, "secret", "test-secret-key-for-unit-tests");
        user = new User("testuser", "password123", UserRole.EMPLOYEE);
    }

    @Nested
    @DisplayName("Geração e Validação de Tokens")
    class TokenLifecycle {

        @Test
        @DisplayName("Deve gerar um token válido para um utilizador")
        void shouldGenerateToken() {
            String token = tokenService.generateToken(user);
            assertNotNull(token);
            assertFalse(token.isEmpty());
        }

        @Test
        @DisplayName("Deve validar um token correto e retornar o username")
        void shouldValidateCorrectToken() {
            String token = tokenService.generateToken(user);
            String subject = tokenService.validateToken(token);
            assertEquals(user.getUsername(), subject);
        }

        @Test
        @DisplayName("Deve lançar TokenInvalidException para token forjado ou inválido")
        void shouldThrowExceptionForInvalidToken() {
            String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.invalid.payload";
            assertThrows(TokenInvalidException.class, () -> tokenService.validateToken(invalidToken));
        }
    }
}