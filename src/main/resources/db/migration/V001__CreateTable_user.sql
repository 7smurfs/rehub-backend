-- Create table schema for rehub user
-- username column is user email. There is no need for actual username
-- username column represents hash user email
-- password column has size limitation for ByCrypt password hashing

CREATE TABLE IF NOT EXISTS rehub_user
(
    id       BIGSERIAL PRIMARY KEY,
    username TEXT        NOT NULL,
    password VARCHAR(60) NOT NULL,
    status   TEXT        NOT NULL
);

-- username needs to be unique
ALTER TABLE rehub_user
    ADD CONSTRAINT uc_rehub_user_username UNIQUE (username);
