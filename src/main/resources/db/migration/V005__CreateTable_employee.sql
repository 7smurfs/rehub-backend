-- Create table schema for employee

CREATE TABLE IF NOT EXISTS employee
(
    id               BIGSERIAL PRIMARY KEY,
    first_name       TEXT                                NOT NULL,
    last_name        TEXT                                NOT NULL,
    phone_number     TEXT                                NOT NULL,
    profession       TEXT                                NOT NULL,
    gender           TEXT                                NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_at TIMESTAMP                           NULL,
    user_id          BIGINT                              NOT NULL,
    FOREIGN KEY (user_id) REFERENCES rehub_user (id)
);

-- phone number needs to be unique
ALTER TABLE employee
    ADD CONSTRAINT uc_employee_phone_number UNIQUE (phone_number);