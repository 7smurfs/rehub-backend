--Create table schema for FAQ

CREATE TABLE IF NOT EXISTS faq
(
    id          BIGSERIAL       PRIMARY KEY,
    question    TEXT            NOT NULL,
    answer      TEXT            NOT NULL

);