# 🅿️ LocusPark API

Sistema de Gestão de Estacionamento — Backend em **Java + Spring Boot**.

---

## 🔐 Autenticação (Stateless / JWT)

- O servidor **não guarda sessão**. O frontend é responsável por armazenar o token (ex.: `localStorage`).
- Para rotas protegidas, envie o token no header:

```http
Authorization: Bearer <SEU_TOKEN_AQUI>
```

- **Logout** é feito no frontend, apagando o token localmente.

---

## 🗺️ Endpoints

**URL Base:** `http://localhost:8080`

| Método | Rota | Auth | Descrição |
|---|---|---|---|
| `POST` | `/auth/register` | ❌ | Cria um novo utilizador |
| `POST` | `/auth/login` | ❌ | Autentica e retorna o JWT |
| `GET` | `/user/profile` | ✅ | Retorna dados do utilizador logado |
| `GET` | `/api/hello` | ✅ | Rota de teste |

### `POST /auth/login` — Exemplo de resposta
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "id": "UUID-DO-UTILIZADOR",
  "username": "lucas",
  "role": "USER"
}
```

---

## ⚠️ Padrão de Erros

Todos os erros retornam o mesmo formato:

```json
{
  "timestamp": "2026-05-12T04:00:00.000Z",
  "status": 401,
  "error": "Token mal formatado, inválido ou expirado.",
  "path": "/user/profile"
}
```

Lê sempre o campo `error` para exibir a mensagem ao utilizador.

---

## ⚙️ Variáveis de Ambiente

Cria o ficheiro `application-local.properties` (já no `.gitignore`). O banco de dados utilizado é **MySQL**, hospedado no **TiDB Cloud**:

```properties
PORT=8080
API_SECURITY_TOKEN_SECRET=sua_chave_secreta
DATABASE_URL=jdbc:mysql://<host>.tidbcloud.com:4000/locuspark
DATABASE_USERNAME=seu_usuario
DATABASE_PASSWORD=sua_senha
```