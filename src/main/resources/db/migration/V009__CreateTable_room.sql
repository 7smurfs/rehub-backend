-- Create table room

CREATE TABLE IF NOT EXISTS room
(
    id              BIGSERIAL PRIMARY KEY,
    label           VARCHAR(255) NOT NULL,
    capacity        INT          NOT NULL,
    status          VARCHAR(255) NOT NULL,
    special_message VARCHAR(255) NULL
);

-- name needs to be unique
ALTER TABLE room
    ADD CONSTRAINT uc_room_label UNIQUE (label);