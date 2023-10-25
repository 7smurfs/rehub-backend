-- Create table schema for patient
-- pin column is personal identification number
-- phin column is personal health identification number

CREATE TABLE IF NOT EXISTS patient
(
    id               BIGSERIAL PRIMARY KEY,
    first_name       VARCHAR(255)                        NOT NULL,
    last_name        VARCHAR(255)                        NOT NULL,
    pin              VARCHAR(64)                         NOT NULL,
    phin             VARCHAR(64)                         NOT NULL,
    gender           VARCHAR(255)                        NULL,
    phone_number     VARCHAR                             NOT NULL,
    date_of_birth    DATE                                NOT NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_at TIMESTAMP                           NULL,
    user_id          BIGINT                              NOT NULL,
    FOREIGN KEY (user_id) REFERENCES rehub_user (id)
);

-- pin needs to be unique
ALTER TABLE patient
    ADD CONSTRAINT uc_patient_pin UNIQUE (pin);

-- phin needs to be unique
ALTER TABLE patient
    ADD CONSTRAINT uc_patient_phin UNIQUE (phin);

-- phone number needs to be unique
ALTER TABLE patient
    ADD CONSTRAINT uc_patient_phone_number UNIQUE (phone_number);