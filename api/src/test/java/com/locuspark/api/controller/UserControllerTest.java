package com.locuspark.api.controller;

import com.locuspark.api.entity.User;
import com.locuspark.api.enums.UserRole;
import com.locuspark.api.repository.UserRepository;
import com.locuspark.api.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    @MockBean
    private UserRepository userRepository;

    private String validToken;
    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User("testuser", "password", UserRole.USER);
        // Precisamos mockar o repositório porque o SecurityFilter chama findByUsername
        when(userRepository.findByUsername("testuser")).thenReturn(mockUser);

        // Geramos um token real usando o serviço real injetado
        validToken = tokenService.generateToken(mockUser);
    }

    @Test
    @DisplayName("Deve retornar 200 OK e perfil ao fornecer um token JWT válido")
    void shouldReturnProfileWithValidToken() throws Exception {
        mockMvc.perform(get("/user/profile")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    @DisplayName("Deve retornar 401 Unauthorized quando o token não é fornecido")
    void shouldReturn401WithoutToken() throws Exception {
        mockMvc.perform(get("/user/profile"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Token não fornecido ou cabeçalho Authorization ausente."));
    }

    @Test
    @DisplayName("Deve retornar 401 Unauthorized com mensagem de erro para token inválido")
    void shouldReturn401WithInvalidToken() throws Exception {
        mockMvc.perform(get("/user/profile")
                        .header("Authorization", "Bearer token-invalido-forjado"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Token mal formatado, inválido ou expirado."));
    }
}