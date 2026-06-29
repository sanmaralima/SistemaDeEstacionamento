package com.locuspark.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.locuspark.api.dto.request.ClientRequest;
import com.locuspark.api.dto.response.ClientResponse;
import com.locuspark.api.entity.Company;
import com.locuspark.api.entity.User;
import com.locuspark.api.enums.ClientType;
import com.locuspark.api.enums.UserRole;
import com.locuspark.api.repository.UserRepository;
import com.locuspark.api.security.TokenService;
import com.locuspark.api.service.ClientService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes de Integração do Controlador de Clientes - ClientController")
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenService tokenService;

    @MockitoBean
    private ClientService clientService;

    @MockitoBean
    private UserRepository userRepository;

    private String adminToken;
    private String employeeToken;

    private final UUID companyId = UUID.randomUUID();
    private final UUID clientId = UUID.randomUUID();

    private Company company;
    private ClientRequest clientRequest;
    private ClientRequest invalidClientRequest;
    private ClientResponse clientResponse;

    @BeforeEach
    void setUp() {
        company = Company.builder()
                .id(companyId)
                .name("Estacionamento Central")
                .build();

        User adminUser = new User("admin", "pass", UserRole.ADMIN, company);
        User employeeUser = new User("employee", "pass", UserRole.EMPLOYEE, company);

        when(userRepository.findByUsername("admin")).thenReturn(adminUser);
        when(userRepository.findByUsername("employee")).thenReturn(employeeUser);

        adminToken = tokenService.generateToken(adminUser);
        employeeToken = tokenService.generateToken(employeeUser);

        clientRequest = new ClientRequest(
                "João Silva",
                "01234567890", // CPF válido
                "11999999999",
                ClientType.MENSALISTA
        );

        invalidClientRequest = new ClientRequest(
                "João Silva",
                "123", // CPF inválido (tamanho menor e formato incorreto)
                "11999999999",
                ClientType.MENSALISTA
        );

        clientResponse = new ClientResponse(
                clientId,
                "João Silva",
                "01234567890",
                "11999999999",
                ClientType.MENSALISTA,
                companyId
        );
    }

    @Nested
    @DisplayName("Cenários de Cadastro (POST /clients)")
    class CreateClients {

        @Test
        @WithMockUser(roles = "EMPLOYEE")
        @DisplayName("Deve permitir a criação de cliente para funcionários logados (HTTP 201 Created)")
        void createClientSuccess() throws Exception {
            when(clientService.createClient(eq(companyId), any(ClientRequest.class))).thenReturn(clientResponse);

            mockMvc.perform(post("/clients")
                    .header("Authorization", "Bearer " + employeeToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(clientRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(clientId.toString()))
                    .andExpect(jsonPath("$.companyId").value(companyId.toString()))
                    .andExpect(jsonPath("$.cpf").value("01234567890"));

            verify(clientService).createClient(eq(companyId), any(ClientRequest.class));
        }

        @Test
        @DisplayName("Deve negar a criação de cliente se o token de autorização estiver ausente (HTTP 401 Unauthorized)")
        void createClientFailUnauthorized() throws Exception {
            mockMvc.perform(post("/clients")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(clientRequest)))
                    .andExpect(status().isUnauthorized());

            verify(clientService, never()).createClient(any(UUID.class), any(ClientRequest.class));
        }

        @Test
        @WithMockUser(roles = "EMPLOYEE")
        @DisplayName("Deve retornar HTTP 400 Bad Request ao tentar cadastrar payload inválido (CPF mal formatado)")
        void createClientFailInvalidPayload() throws Exception {
            mockMvc.perform(post("/clients")
                    .header("Authorization", "Bearer " + employeeToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidClientRequest)))
                    .andExpect(status().isBadRequest());

            verify(clientService, never()).createClient(any(UUID.class), any(ClientRequest.class));
        }
    }

    @Nested
    @DisplayName("Cenários de Listagem (GET /clients)")
    class ListClients {

        @Test
        @WithMockUser(roles = "EMPLOYEE")
        @DisplayName("Deve interceptar o token, decodificar a empresa logada e retornar a lista de clientes (HTTP 200 OK)")
        void listClientsSuccess() throws Exception {
            when(clientService.listAllClientsByCompany(companyId)).thenReturn(List.of(clientResponse));

            mockMvc.perform(get("/clients")
                    .header("Authorization", "Bearer " + employeeToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(clientId.toString()))
                    .andExpect(jsonPath("$[0].companyId").value(companyId.toString()));

            verify(clientService).listAllClientsByCompany(companyId);
        }
    }

    @Nested
    @DisplayName("Cenários de Busca por ID (GET /clients/{id})")
    class GetClient {

        @Test
        @WithMockUser(roles = "EMPLOYEE")
        @DisplayName("Deve retornar os detalhes do cliente se pertencer ao mesmo tenant logado (HTTP 200 OK)")
        void getClientSuccess() throws Exception {
            when(clientService.getClientByIdAndCompany(clientId, companyId)).thenReturn(clientResponse);

            mockMvc.perform(get("/clients/" + clientId)
                    .header("Authorization", "Bearer " + employeeToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(clientId.toString()))
                    .andExpect(jsonPath("$.companyId").value(companyId.toString()));

            verify(clientService).getClientByIdAndCompany(clientId, companyId);
        }
    }

    @Nested
    @DisplayName("Cenários de Atualização (PUT /clients/{id})")
    class UpdateClient {

        @Test
        @WithMockUser(roles = "EMPLOYEE")
        @DisplayName("Deve permitir a edição do cliente da mesma empresa logada (HTTP 200 OK)")
        void updateClientSuccess() throws Exception {
            when(clientService.updateClient(eq(clientId), eq(companyId), any(ClientRequest.class))).thenReturn(clientResponse);

            mockMvc.perform(put("/clients/" + clientId)
                    .header("Authorization", "Bearer " + employeeToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(clientRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(clientId.toString()))
                    .andExpect(jsonPath("$.companyId").value(companyId.toString()));

            verify(clientService).updateClient(eq(clientId), eq(companyId), any(ClientRequest.class));
        }
    }

    @Nested
    @DisplayName("Cenários de Exclusão (DELETE /clients/{id})")
    class DeleteClient {

        @Test
        @WithMockUser(roles = "ADMIN")
        @DisplayName("Deve permitir a exclusão do cliente para o token de ADMIN da empresa (HTTP 204 No Content)")
        void deleteClientSuccess() throws Exception {
            doNothing().when(clientService).deleteClient(clientId, companyId);

            mockMvc.perform(delete("/clients/" + clientId)
                    .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isNoContent());

            verify(clientService).deleteClient(clientId, companyId);
        }

        @Test
        @WithMockUser(roles = "EMPLOYEE")
        @DisplayName("Deve negar a exclusão do cliente para funcionários que não sejam ADMIN (HTTP 403 Forbidden)")
        void deleteClientFailForbiddenForEmployee() throws Exception {
            mockMvc.perform(delete("/clients/" + clientId)
                    .header("Authorization", "Bearer " + employeeToken))
                    .andExpect(status().isForbidden());

            verify(clientService, never()).deleteClient(any(UUID.class), any(UUID.class));
        }
    }
}
