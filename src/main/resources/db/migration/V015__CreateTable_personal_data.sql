-- Create table personal_data that will mock DB for people personal information

CREATE TABLE IF NOT EXISTS personal_data
(
    id            BIGSERIAL PRIMARY KEY,
    first_name    TEXT        NOT NULL,
    last_name     TEXT        NOT NULL,
    pin           VARCHAR(64) NOT NULL,
    phin          VARCHAR(64) NULL,
    date_of_birth DATE        NOT NULL
);

-- pin needs to be unique
ALTER TABLE personal_data
    ADD CONSTRAINT uc_personal_data_pin UNIQUE (pin);

-- phin needs to be unique
ALTER TABLE personal_data
    ADD CONSTRAINT uc_personal_data_phin UNIQUE (phin);