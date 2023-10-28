--Create table schema for FAQ

CREATE TABLE IF NOT EXISTS faq
(
    id            BIGSERIAL PRIMARY KEY,
    question    VARCHAR(255) NOT NULL,
    answer    VARCHAR(255) NOT NULL

);