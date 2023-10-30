-- Create table schema for doctor
-- pin column is personal identification number
-- medical_no column is unique number of medical institution worker

CREATE TABLE IF NOT EXISTS doctor
(
    id                  BIGSERIAL PRIMARY KEY,
    first_name          TEXT        NOT NULL,
    last_name           TEXT        NOT NULL,
    pin                 VARCHAR(64) NOT NULL,
    medical_no          VARCHAR(64) NOT NULL,
    speciality          TEXT        NOT NULL,
    date_of_employment  DATE        NULL,
    date_of_resignation DATE        NULL,
    date_of_birth       DATE        NOT NULL
);

-- pin needs to be unique
ALTER TABLE doctor
    ADD CONSTRAINT uc_doctor_pin UNIQUE (pin);

-- medical worker number needs to be unique
ALTER TABLE doctor
    ADD CONSTRAINT uc_doctor_medical_no UNIQUE (medical_no);