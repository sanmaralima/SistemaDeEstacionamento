package com.locuspark.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.locuspark.api.dto.request.CompanyRequest;
import com.locuspark.api.dto.response.CompanyResponse;
import com.locuspark.api.entity.User;
import com.locuspark.api.enums.CompanyStatus;
import com.locuspark.api.enums.UserRole;
import com.locuspark.api.repository.UserRepository;
import com.locuspark.api.security.TokenService;
import com.locuspark.api.service.CompanyService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenService tokenService;

    @MockitoBean
    private CompanyService companyService;

    @MockitoBean
    private UserRepository userRepository;

    private String superAdminToken;
    private String adminToken;
    private String employeeToken;
    private final UUID id = UUID.randomUUID();
    private final CompanyRequest request = new CompanyRequest("Locus", "06990590000123", 10);
    private final CompanyResponse response = new CompanyResponse(id, "Locus", "06990590000123", 10, CompanyStatus.ACTIVE);

    @BeforeEach
    void setUp() {
        User sa = new User("sa", "pass", UserRole.SUPER_ADMIN);
        User adm = new User("adm", "pass", UserRole.ADMIN);
        User emp = new User("emp", "pass", UserRole.EMPLOYEE);

        when(userRepository.findByUsername("sa")).thenReturn(sa);
        when(userRepository.findByUsername("adm")).thenReturn(adm);
        when(userRepository.findByUsername("emp")).thenReturn(emp);

        superAdminToken = tokenService.generateToken(sa);
        adminToken = tokenService.generateToken(adm);
        employeeToken = tokenService.generateToken(emp);
    }

    @Nested
    @DisplayName("Criação de Empresas")
    class Create {
        @Test
        @WithMockUser(roles = "SUPER_ADMIN")
        @DisplayName("Deve permitir criar empresa para SUPER_ADMIN")
        void createSuperAdmin() throws Exception {
            when(companyService.createCompany(request)).thenReturn(response);

            mockMvc.perform(post("/companies")
                    .header("Authorization", "Bearer " + superAdminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(id.toString()))
                    .andExpect(jsonPath("$.cnpj").value("06990590000123"));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        @DisplayName("Deve negar criação para ADMIN")
        void createAdmin() throws Exception {
            mockMvc.perform(post("/companies")
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("Listagem e Busca")
    class GetRoutes {
        @Test
        @WithMockUser(roles = "SUPER_ADMIN")
        @DisplayName("Deve permitir listagem completa para SUPER_ADMIN")
        void listSuperAdmin() throws Exception {
            when(companyService.listAllCompanies()).thenReturn(List.of(response));

            mockMvc.perform(get("/companies").header("Authorization", "Bearer " + superAdminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(id.toString()));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        @DisplayName("Deve permitir buscar por ID para ADMIN")
        void getByIdAdmin() throws Exception {
            when(companyService.getCompanyById(id)).thenReturn(response);

            mockMvc.perform(get("/companies/" + id).header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id.toString()));
        }

        @Test
        @WithMockUser(roles = "EMPLOYEE")
        @DisplayName("Deve negar buscar por ID para EMPLOYEE")
        void getByIdEmployee() throws Exception {
            mockMvc.perform(get("/companies/" + id).header("Authorization", "Bearer " + employeeToken))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("Atualização e Remoção")
    class PutDelete {
        @Test
        @WithMockUser(roles = "ADMIN")
        @DisplayName("Deve permitir atualizar empresa para ADMIN")
        void updateAdmin() throws Exception {
            when(companyService.updateCompany(id, request)).thenReturn(response);

            mockMvc.perform(put("/companies/" + id)
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id.toString()));
        }

        @Test
        @WithMockUser(roles = "SUPER_ADMIN")
        @DisplayName("Deve permitir inativação para SUPER_ADMIN")
        void deleteSuperAdmin() throws Exception {
            mockMvc.perform(delete("/companies/" + id).header("Authorization", "Bearer " + superAdminToken))
                    .andExpect(status().isNoContent());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        @DisplayName("Deve negar inativação para ADMIN")
        void deleteAdmin() throws Exception {
            mockMvc.perform(delete("/companies/" + id).header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isForbidden());
        }
    }
}
