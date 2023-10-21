-- Create table equipment
-- Equipment needs to have room fk, that means every equipment is assigned to room

CREATE TABLE equipment
(
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    status          VARCHAR(255) NOT NULL,
    special_message VARCHAR(255) NULL,
    room_id         BIGINT       NOT NULL,
    FOREIGN KEY (room_id) REFERENCES room (id)
);