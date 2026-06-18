CREATE TABLE clients
(
    id         BINARY(16)   NOT NULL,
    name       VARCHAR(255) NOT NULL,
    cpf        VARCHAR(11)  NOT NULL,
    phone      VARCHAR(255) NOT NULL,
    type       VARCHAR(255) NOT NULL,
    company_id BINARY(16)   NOT NULL,
    CONSTRAINT pk_clients PRIMARY KEY (id)
);

CREATE TABLE companies
(
    id          BINARY(16)   NOT NULL,
    name        VARCHAR(255) NOT NULL,
    cnpj        VARCHAR(14)  NOT NULL,
    total_spots INT          NOT NULL,
    status      VARCHAR(255) NOT NULL,
    created_at  datetime NULL,
    updated_at  datetime NULL,
    CONSTRAINT pk_companies PRIMARY KEY (id)
);

CREATE TABLE users
(
    id         BINARY(16)   NOT NULL,
    username   VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    `role`     VARCHAR(255) NOT NULL,
    company_id BINARY(16)   NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE vehicles
(
    id         BINARY(16)   NOT NULL,
    plate      VARCHAR(7)   NOT NULL,
    model      VARCHAR(255) NOT NULL,
    color      VARCHAR(255) NOT NULL,
    company_id BINARY(16)   NOT NULL,
    client_id  BINARY(16)   NULL,
    CONSTRAINT pk_vehicles PRIMARY KEY (id)
);

ALTER TABLE companies
    ADD CONSTRAINT uc_companies_cnpj UNIQUE (cnpj);

ALTER TABLE vehicles
    ADD CONSTRAINT uc_da3eb7e3026176c18b51dc373 UNIQUE (plate, company_id);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

ALTER TABLE clients
    ADD CONSTRAINT FK_CLIENTS_ON_COMPANY FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_COMPANY FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE vehicles
    ADD CONSTRAINT FK_VEHICLES_ON_CLIENT FOREIGN KEY (client_id) REFERENCES clients (id);

ALTER TABLE vehicles
    ADD CONSTRAINT FK_VEHICLES_ON_COMPANY FOREIGN KEY (company_id) REFERENCES companies (id);