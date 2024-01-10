-- Create table verification for user

CREATE TABLE IF NOT EXISTS verification
(
    id      BIGSERIAL PRIMARY KEY,
    token   VARCHAR(36) NOT NULL,
    status  TEXT        NOT NULL,
    user_id BIGINT      NOT NULL,
    FOREIGN KEY (user_id) REFERENCES rehub_user (id)
);

-- token needs to be unique
ALTER TABLE verification
    ADD CONSTRAINT uc_verification_token UNIQUE (token);