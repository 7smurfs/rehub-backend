-- Create table schema for patient
-- pin column is personal identification number

CREATE TABLE IF NOT EXISTS patient
(
    id            BIGSERIAL PRIMARY KEY,
    first_name    VARCHAR(255) NOT NULL,
    last_name     VARCHAR(255) NOT NULL,
    pin           VARCHAR(11)  NOT NULL,
    gender        VARCHAR(255) NULL,
    date_of_birth TIMESTAMP    NOT NULL
);

-- pin needs to be unique
ALTER TABLE patient
    ADD CONSTRAINT uc_patient_pin UNIQUE (pin);