-- Create table appointment

CREATE TABLE IF NOT EXISTS appointment
(
    id       BIGSERIAL PRIMARY KEY,
    start_at TIMESTAMP NOT NULL,
    end_at   TIMESTAMP NOT NULL
);