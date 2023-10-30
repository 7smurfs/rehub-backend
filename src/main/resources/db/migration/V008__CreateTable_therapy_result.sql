-- Create table schema for therapy result

CREATE TABLE IF NOT EXISTS therapy_result
(
    id     BIGSERIAL PRIMARY KEY,
    status TEXT NOT NULL,
    result TEXT NOT NULL
);