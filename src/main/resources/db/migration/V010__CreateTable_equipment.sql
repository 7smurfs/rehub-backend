-- Create table equipment
-- Equipment needs to have room fk, that means every equipment is assigned to room

CREATE TABLE equipment
(
    id              BIGSERIAL PRIMARY KEY,
    name            TEXT   NOT NULL,
    status          TEXT   NOT NULL,
    special_message TEXT   NULL,
    room_id         BIGINT NOT NULL,
    FOREIGN KEY (room_id) REFERENCES room (id)
);