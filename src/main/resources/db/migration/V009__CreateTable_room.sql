-- Create table room

CREATE TABLE IF NOT EXISTS room
(
    id              BIGSERIAL PRIMARY KEY,
    label           TEXT NOT NULL,
    capacity        INT  NOT NULL,
    status          TEXT NOT NULL,
    special_message TEXT NULL
);

-- name needs to be unique
ALTER TABLE room
    ADD CONSTRAINT uc_room_label UNIQUE (label);