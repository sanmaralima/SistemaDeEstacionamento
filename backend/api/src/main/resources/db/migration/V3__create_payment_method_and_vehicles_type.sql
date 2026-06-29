ALTER TABLE tickets
    ADD payment_method VARCHAR(255) NULL;

ALTER TABLE vehicles
    ADD type VARCHAR(255) NULL;

ALTER TABLE vehicles
    MODIFY type VARCHAR (255) NOT NULL;

ALTER TABLE tariff_configurations
    MODIFY additional_fraction_value DECIMAL (10, 2) NULL;

ALTER TABLE tariff_configurations
    MODIFY first_hour_value DECIMAL (10, 2) NULL;

ALTER TABLE tariff_configurations
    MODIFY lost_ticket_fee DECIMAL (10, 2) NULL;

ALTER TABLE tariff_configurations
    MODIFY overnight_fee DECIMAL (10, 2) NULL;

ALTER TABLE tariff_configurations
    MODIFY tolerance_minutes INT NULL;