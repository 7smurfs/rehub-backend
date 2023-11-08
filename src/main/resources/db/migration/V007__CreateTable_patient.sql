-- Create table schema for patient

CREATE TABLE IF NOT EXISTS patient
(
    id               BIGSERIAL PRIMARY KEY,
    first_name       TEXT                                NOT NULL,
    last_name        TEXT                                NOT NULL,
    gender           TEXT                                NULL,
    phone_number     TEXT                                NOT NULL,
    date_of_birth    DATE                                NOT NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_at TIMESTAMP                           NULL,
    user_id          BIGINT                              NOT NULL,
    FOREIGN KEY (user_id) REFERENCES rehub_user (id)
);

-- phone number needs to be unique
ALTER TABLE patient
    ADD CONSTRAINT uc_patient_phone_number UNIQUE (phone_number);