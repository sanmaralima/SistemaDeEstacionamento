package com.locuspark.api.service;

import com.locuspark.api.types.Cpf;
import com.locuspark.api.dto.request.ClientRequest;
import com.locuspark.api.dto.response.ClientResponse;
import com.locuspark.api.entity.Client;
import com.locuspark.api.entity.Company;
import com.locuspark.api.enums.ClientType;
import com.locuspark.api.exception.BusinessException;
import com.locuspark.api.mapper.ClientMapper;
import com.locuspark.api.repository.ClientRepository;
import com.locuspark.api.repository.CompanyRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de Serviço do Cliente - ClientService")
class ClientServiceTest {

    @Mock
    private ClientRepository repository;

    @Mock
    private ClientMapper mapper;

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private ClientService service;

    private final UUID companyId = UUID.randomUUID();
    private final UUID clientId = UUID.randomUUID();

    private Company company;
    private ClientRequest clientRequest;
    private Client client;
    private ClientResponse clientResponse;

    @BeforeEach
    void setUp() {
        company = Company.builder()
                .id(companyId)
                .name("Empresa Logada")
                .build();

        clientRequest = new ClientRequest(
                "João Silva",
                "01234567890",
                "11999999999",
                ClientType.MENSALISTA
        );

        client = Client.builder()
                .id(clientId)
                .name("João Silva")
                .cpf(new Cpf("01234567890"))
                .phone("11999999999")
                .type(ClientType.MENSALISTA)
                .company(company)
                .build();

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
    @DisplayName("Cenários de Cadastro de Clientes (createClient)")
    class CreateClient {

        @Test
        @DisplayName("Deve salvar e retornar um ClientResponse com sucesso vinculado à empresa logada")
        void createClientSuccess() {
            // Arrange
            when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
            when(repository.existsByCpfAndCompanyId(new Cpf(clientRequest.cpf()), companyId)).thenReturn(false);
            when(mapper.toEntity(clientRequest)).thenReturn(client);
            when(repository.save(client)).thenReturn(client);
            when(mapper.toResponse(client)).thenReturn(clientResponse);

            // Act
            ClientResponse result = service.createClient(companyId, clientRequest);

            // Assert
            assertNotNull(result);
            assertEquals(clientId, result.id());
            assertEquals(companyId, result.companyId());
            assertEquals(clientRequest.cpf(), result.cpf());
            assertEquals(clientRequest.name(), result.name());
            
            verify(companyRepository).findById(companyId);
            verify(repository).existsByCpfAndCompanyId(new Cpf(clientRequest.cpf()), companyId);
            verify(repository).save(client);
        }

        @Test
        @DisplayName("Deve lançar BusinessException se a empresa não for encontrada")
        void createClientFailCompanyNotFound() {
            // Arrange
            when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

            // Act & Assert
            BusinessException exception = assertThrows(BusinessException.class, () ->
                    service.createClient(companyId, clientRequest)
            );

            assertEquals("Empresa não encontrada.", exception.getMessage());
            verify(companyRepository).findById(companyId);
            verify(repository, never()).existsByCpfAndCompanyId(any(Cpf.class), any(UUID.class));
            verify(repository, never()).save(any(Client.class));
        }

        @Test
        @DisplayName("Deve lançar BusinessException se o CPF já estiver cadastrado NA MESMA empresa")
        void createClientFailCpfAlreadyExists() {
            // Arrange
            when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
            when(repository.existsByCpfAndCompanyId(new Cpf(clientRequest.cpf()), companyId)).thenReturn(true);

            // Act & Assert
            BusinessException exception = assertThrows(BusinessException.class, () ->
                    service.createClient(companyId, clientRequest)
            );

            assertEquals("Já existe um cliente cadastrado com este CPF nesta empresa.", exception.getMessage());
            verify(companyRepository).findById(companyId);
            verify(repository).existsByCpfAndCompanyId(new Cpf(clientRequest.cpf()), companyId);
            verify(repository, never()).save(any(Client.class));
        }
    }

    @Nested
    @DisplayName("Cenários de Listagem de Clientes (listAllClientsByCompany)")
    class ListAllClientsByCompany {

        @Test
        @DisplayName("Deve retornar a lista de clientes filtrada estritamente pelo companyId")
        void listAllClientsSuccess() {
            // Arrange
            List<Client> clients = List.of(client);
            when(repository.findByCompanyId(companyId)).thenReturn(clients);
            when(mapper.toResponse(client)).thenReturn(clientResponse);

            // Act
            List<ClientResponse> result = service.listAllClientsByCompany(companyId);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(clientResponse, result.get(0));

            verify(repository).findByCompanyId(companyId);
            verify(mapper).toResponse(client);
        }
    }

    @Nested
    @DisplayName("Cenários de Busca de Cliente por ID e Empresa (getClientByIdAndCompany)")
    class GetClientByIdAndCompany {

        @Test
        @DisplayName("Deve retornar o cliente com sucesso se ele pertencer à empresa informada")
        void getClientByIdAndCompanySuccess() {
            // Arrange
            when(repository.findByIdAndCompanyId(clientId, companyId)).thenReturn(Optional.of(client));
            when(mapper.toResponse(client)).thenReturn(clientResponse);

            // Act
            ClientResponse result = service.getClientByIdAndCompany(clientId, companyId);

            // Assert
            assertNotNull(result);
            assertEquals(clientId, result.id());
            assertEquals(companyId, result.companyId());

            verify(repository).findByIdAndCompanyId(clientId, companyId);
        }

        @Test
        @DisplayName("Deve lançar BusinessException se o cliente não existir ou não pertencer àquela empresa")
        void getClientByIdAndCompanyFailNotFoundOrAccessDenied() {
            // Arrange
            when(repository.findByIdAndCompanyId(clientId, companyId)).thenReturn(Optional.empty());

            // Act & Assert
            BusinessException exception = assertThrows(BusinessException.class, () ->
                    service.getClientByIdAndCompany(clientId, companyId)
            );

            assertEquals("Cliente não encontrado ou não pertence a esta empresa.", exception.getMessage());
            verify(repository).findByIdAndCompanyId(clientId, companyId);
            verify(mapper, never()).toResponse(any(Client.class));
        }
    }

    @Nested
    @DisplayName("Cenários de Atualização de Cliente (updateClient)")
    class UpdateClient {

        @Test
        @DisplayName("Deve atualizar os dados do cliente com sucesso se ele for daquela empresa e o CPF não for duplicado")
        void updateClientSuccess() {
            // Arrange
            ClientRequest updateRequest = new ClientRequest(
                    "João Silva Alterado",
                    "01234567890",
                    "11988888888",
                    ClientType.AVULSO
            );
            
            Client updatedClient = Client.builder()
                    .id(clientId)
                    .name(updateRequest.name())
                    .cpf(new Cpf("01234567890"))
                    .phone(updateRequest.phone())
                    .type(updateRequest.type())
                    .company(company)
                    .build();

            ClientResponse updatedResponse = new ClientResponse(
                    clientId,
                    updateRequest.name(),
                    updateRequest.cpf(),
                    updateRequest.phone(),
                    updateRequest.type(),
                    companyId
            );

            when(repository.findByIdAndCompanyId(clientId, companyId)).thenReturn(Optional.of(client));
            when(repository.save(any(Client.class))).thenReturn(updatedClient);
            when(mapper.toResponse(updatedClient)).thenReturn(updatedResponse);

            // Act
            ClientResponse result = service.updateClient(clientId, companyId, updateRequest);

            // Assert
            assertNotNull(result);
            assertEquals(updateRequest.name(), result.name());
            assertEquals(updateRequest.phone(), result.phone());
            assertEquals(updateRequest.type(), result.type());

            verify(repository).findByIdAndCompanyId(clientId, companyId);
            verify(repository).save(any(Client.class));
        }

        @Test
        @DisplayName("Deve lançar BusinessException na atualização se o cliente não for encontrado naquela empresa")
        void updateClientFailNotFound() {
            // Arrange
            when(repository.findByIdAndCompanyId(clientId, companyId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(BusinessException.class, () ->
                    service.updateClient(clientId, companyId, clientRequest)
            );

            verify(repository).findByIdAndCompanyId(clientId, companyId);
            verify(repository, never()).save(any(Client.class));
        }

        @Test
        @DisplayName("Deve lançar BusinessException na atualização se tentar usar um CPF de outro cliente cadastrado na mesma empresa")
        void updateClientFailCpfAlreadyExists() {
            // Arrange
            ClientRequest updateRequest = new ClientRequest(
                    "Outro Nome",
                    "98765432100", // CPF alterado, que pertence a outro cliente
                    "11988888888",
                    ClientType.AVULSO
            );

            when(repository.findByIdAndCompanyId(clientId, companyId)).thenReturn(Optional.of(client));
            when(repository.existsByCpfAndCompanyId(new Cpf(updateRequest.cpf()), companyId)).thenReturn(true);

            // Act & Assert
            BusinessException exception = assertThrows(BusinessException.class, () ->
                    service.updateClient(clientId, companyId, updateRequest)
            );

            assertEquals("Já existe outro cliente cadastrado com este CPF nesta empresa.", exception.getMessage());
            verify(repository).findByIdAndCompanyId(clientId, companyId);
            verify(repository).existsByCpfAndCompanyId(new Cpf(updateRequest.cpf()), companyId);
            verify(repository, never()).save(any(Client.class));
        }
    }

    @Nested
    @DisplayName("Cenários de Exclusão de Cliente (deleteClient)")
    class DeleteClient {

        @Test
        @DisplayName("Deve remover fisicamente o cliente com sucesso se pertencer à empresa informada")
        void deleteClientSuccess() {
            // Arrange
            when(repository.findByIdAndCompanyId(clientId, companyId)).thenReturn(Optional.of(client));
            doNothing().when(repository).delete(client);

            // Act
            assertDoesNotThrow(() -> service.deleteClient(clientId, companyId));

            // Assert
            verify(repository).findByIdAndCompanyId(clientId, companyId);
            verify(repository).delete(client);
        }

        @Test
        @DisplayName("Deve lançar BusinessException na exclusão se o cliente não for encontrado ou pertencer a outra empresa")
        void deleteClientFailNotFound() {
            // Arrange
            when(repository.findByIdAndCompanyId(clientId, companyId)).thenReturn(Optional.empty());

            // Act & Assert
            BusinessException exception = assertThrows(BusinessException.class, () ->
                    service.deleteClient(clientId, companyId)
            );

            assertEquals("Cliente não encontrado ou não pertence a esta empresa.", exception.getMessage());
            verify(repository).findByIdAndCompanyId(clientId, companyId);
            verify(repository, never()).delete(any(Client.class));
        }
    }
}
