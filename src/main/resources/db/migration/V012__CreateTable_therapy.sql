-- Create table therapy

CREATE TABLE IF NOT EXISTS therapy
(
    id                BIGSERIAL PRIMARY KEY,
    type              VARCHAR(255)                        NOT NULL,
    request           VARCHAR                             NOT NULL,
    status            VARCHAR(255)                        NOT NULL,
    ref_id            BIGINT                              NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_at  TIMESTAMP                           NOT NULL,
    patient_id        BIGINT                              NOT NULL,
    room_id           BIGINT                              NOT NULL,
    doctor_id         BIGINT                              NOT NULL,
    appointment_id    BIGINT                              NOT NULL,
    therapy_result_id BIGINT                              NULL,
    FOREIGN KEY (patient_id) REFERENCES patient (id),
    FOREIGN KEY (room_id) REFERENCES room (id),
    FOREIGN KEY (doctor_id) REFERENCES doctor (id),
    FOREIGN KEY (appointment_id) REFERENCES appointment (id),
    FOREIGN KEY (therapy_result_id) REFERENCES therapy_result (id)
);