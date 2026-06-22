CREATE TABLE partnerships
(
    id            BINARY(16)     NOT NULL,
    company_id    BINARY(16)     NOT NULL,
    name          VARCHAR(255)   NOT NULL,
    discount_type VARCHAR(255)   NOT NULL,
    value         DECIMAL(10, 2) NOT NULL,
    CONSTRAINT pk_partnerships PRIMARY KEY (id)
);

CREATE TABLE pricing_configurations
(
    id                  BINARY(16)     NOT NULL,
    company_id          BINARY(16)     NOT NULL,
    daily_trigger_hours INT            NOT NULL,
    daily_value         DECIMAL(10, 2) NOT NULL,
    monthly_base_value  DECIMAL(10, 2) NOT NULL,
    CONSTRAINT pk_pricing_configurations PRIMARY KEY (id)
);

CREATE TABLE tariff_configurations
(
    id                        BINARY(16)     NOT NULL,
    company_id                BINARY(16)     NOT NULL,
    tolerance_minutes         INT            NOT NULL,
    first_hour_value          DECIMAL(10, 2) NOT NULL,
    additional_fraction_value DECIMAL(10, 2) NOT NULL,
    overnight_fee             DECIMAL(10, 2) NOT NULL,
    lost_ticket_fee           DECIMAL(10, 2) NOT NULL,
    CONSTRAINT pk_tariff_configurations PRIMARY KEY (id)
);

CREATE TABLE tickets
(
    id             BINARY(16)     NOT NULL,
    company_id     BINARY(16)     NOT NULL,
    vehicle_id     BINARY(16)     NOT NULL,
    partnership_id BINARY(16)     NULL,
    entered_at     datetime     NOT NULL,
    exited_at      datetime NULL,
    status         VARCHAR(255) NOT NULL,
    total_amount   DECIMAL(10, 2) NULL,
    CONSTRAINT pk_tickets PRIMARY KEY (id)
);

ALTER TABLE partnerships
    ADD CONSTRAINT FK_PARTNERSHIPS_ON_COMPANY FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE pricing_configurations
    ADD CONSTRAINT FK_PRICING_CONFIGURATIONS_ON_COMPANY FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE tariff_configurations
    ADD CONSTRAINT FK_TARIFF_CONFIGURATIONS_ON_COMPANY FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE tickets
    ADD CONSTRAINT FK_TICKETS_ON_COMPANY FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE tickets
    ADD CONSTRAINT FK_TICKETS_ON_PARTNERSHIP FOREIGN KEY (partnership_id) REFERENCES partnerships (id);

ALTER TABLE tickets
    ADD CONSTRAINT FK_TICKETS_ON_VEHICLE FOREIGN KEY (vehicle_id) REFERENCES vehicles (id);