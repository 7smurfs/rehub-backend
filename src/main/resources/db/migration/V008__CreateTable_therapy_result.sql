-- Create table schema for therapy result

CREATE TABLE IF NOT EXISTS therapy_result
(
    id     BIGSERIAL PRIMARY KEY,
    status VARCHAR(255) NOT NULL,
    result VARCHAR      NOT NULL
);