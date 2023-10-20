-- Create table schema for doctor
-- pin column is personal identification number

CREATE TABLE IF NOT EXISTS doctor
(
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    pin        VARCHAR(11)  NOT NULL,
    speciality VARCHAR(255) NOT NULL
);

-- pin needs to be unique
ALTER TABLE doctor
    ADD CONSTRAINT uc_doctor_pin UNIQUE (pin);