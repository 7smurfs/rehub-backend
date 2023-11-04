-- Create table schema for password reset
-- token column has size limitation because of UUID generator

CREATE TABLE IF NOT EXISTS password_reset
(
    id                  BIGSERIAL                                    PRIMARY KEY,
    token               VARCHAR(36)                                  NOT NULL,
    status              TEXT                                         NOT NULL,
    user_id             BIGINT                                       NOT NULL,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP          NOT NULL,
    last_modified_at    TIMESTAMP                                    NULL,
    FOREIGN KEY (user_id) REFERENCES rehub_user (id)
);

-- token needs to be unique
ALTER TABLE password_reset
    ADD CONSTRAINT uc_password_reset_token UNIQUE (token);
