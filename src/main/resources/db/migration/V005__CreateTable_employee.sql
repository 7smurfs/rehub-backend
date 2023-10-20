-- Create table schema for employee
-- pin column is personal identification number

CREATE TABLE IF NOT EXISTS employee
(
    id            BIGSERIAL PRIMARY KEY,
    first_name    VARCHAR(255) NOT NULL,
    last_name     VARCHAR(60)  NOT NULL,
    pin           VARCHAR(11)  NOT NULL,
    profession    VARCHAR(255) NOT NULL,
    date_of_birth TIMESTAMP    NOT NULL,
    gender        VARCHAR      NULL,
    user_id       BIGINT       NOT NULL,
    FOREIGN KEY (user_id) REFERENCES rehub_user (id)
);

-- pin needs to be unique
ALTER TABLE employee
    ADD CONSTRAINT uc_employee_pin UNIQUE (pin);