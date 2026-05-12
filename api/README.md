# 🚗 LocusPark API

Backend do sistema **LocusPark**, uma API REST desenvolvida com **Java + Spring Boot** para gerenciamento de estacionamento, autenticação de usuários e controle de acesso utilizando JWT.

---

# 📚 Sumário

- [Tecnologias](#-tecnologias)
- [Arquitetura](#-arquitetura)
- [Autenticação JWT](#-autenticação-jwt)
- [Executando o Projeto](#-executando-o-projeto)
- [Variáveis de Ambiente](#-variáveis-de-ambiente)
- [Endpoints](#-endpoints)
    - [Autenticação](#1-autenticação)
    - [Usuário](#2-usuário)
    - [Teste da API](#3-teste-da-api)
- [Padrão de Respostas](#-padrão-de-respostas)
- [Tratamento de Erros](#-tratamento-de-erros)

---

# 🛠 Tecnologias

- Java 21
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- JPA / Hibernate
- PostgreSQL / MySQL
- Maven

---

# 🏗 Arquitetura

A API segue o padrão **RESTful** utilizando arquitetura **Stateless**.

Isso significa que:

- O servidor **não armazena sessão**
- Cada requisição deve possuir autenticação própria
- O frontend é responsável por armazenar e enviar o token JWT

---

# 🔐 Autenticação JWT

Após realizar login, a API retorna um token JWT.

Exemplo:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

O frontend deve armazenar esse token (localStorage, cookies, etc.) e enviá-lo nas próximas requisições protegidas.

---

## Enviando o Token

```http
Authorization: Bearer SEU_TOKEN_AQUI
```

---

## Logout

Como a API é Stateless:

- Não existe sessão no backend
- Não é necessário endpoint de logout
- O logout consiste apenas em remover o token do frontend

---

# 🚀 Executando o Projeto

## Clone o repositório

```bash
git clone https://github.com/seu-usuario/locuspark-api.git
```

---

## Entre na pasta

```bash
cd locuspark-api
```

---

## Execute o projeto

```bash
./mvnw spring-boot:run
```

Ou no Windows:

```bash
mvnw spring-boot:run
```

---

# ⚙️ Variáveis de Ambiente

Crie um arquivo:

```properties
application-local.properties
```

Ou configure as variáveis no ambiente.

---

## Variáveis necessárias

```properties
PORT=8080

API_SECURITY_TOKEN_SECRET=sua_chave_super_secreta

DATABASE_URL=jdbc:mysql://localhost:3306/locuspark

DATABASE_USERNAME=root
DATABASE_PASSWORD=123456
```

---

# 🌐 Endpoints

URL base local:

```text
http://localhost:8080
```

---

# 1. Autenticação

## 🔹 Registrar usuário

### `POST /auth/register`

Cria um novo usuário.

---

### Body

```json
{
  "username": "lucas",
  "password": "senha123"
}
```

---

### Respostas

#### ✅ 201 Created

Usuário criado com sucesso.

---

#### ❌ 409 Conflict

```json
{
  "error": "Username already exists."
}
```

---

# 🔹 Login

### `POST /auth/login`

Autentica o usuário e retorna um token JWT.

---

### Body

```json
{
  "username": "lucas",
  "password": "senha123"
}
```

---

### Resposta

#### ✅ 200 OK

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "id": "UUID-DO-USUARIO",
  "username": "lucas",
  "role": "USER"
}
```

---

#### ❌ 401 Unauthorized

```json
{
  "error": "Invalid credentials."
}
```

---

# 2. Usuário

## 🔹 Perfil do usuário

### `GET /user/profile`

Retorna os dados do usuário autenticado.

Ideal para reidratação da sessão no frontend.

---

### Headers

```http
Authorization: Bearer SEU_TOKEN
```

---

### Resposta

#### ✅ 200 OK

```json
{
  "id": "UUID-DO-USUARIO",
  "username": "lucas",
  "role": "USER"
}
```

---

#### ❌ 401 Unauthorized

```json
{
  "error": "Token inválido ou expirado."
}
```

---

# 3. Teste da API

## 🔹 Hello World

### `GET /api/hello`

Endpoint de teste da API.

---

### Headers

```http
Authorization: Bearer SEU_TOKEN
```

---

### Resposta

#### ✅ 200 OK

```text
Hello, World!
```

---

# 📦 Padrão de Respostas

A API retorna respostas em JSON.

---

## Sucesso

```json
{
  "data": {}
}
```

---

## Erro

```json
{
  "timestamp": "2026-05-12T04:00:00Z",
  "status": 401,
  "error": "Token inválido ou expirado.",
  "path": "/user/profile"
}
```

---

# ⚠️ Tratamento de Erros

| Status Code | Significado |
|---|---|
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |
| 409 | Conflict |
| 500 | Internal Server Error |

---

# 📌 Observações

- Tokens JWT possuem expiração
- O backend utiliza UTC para validação do token
- Recomenda-se HTTPS em produção
- Nunca exponha sua secret JWT

---

# 👨‍💻 Autor

Desenvolvido por Lucas Almeida 🚀