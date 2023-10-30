-- Create table therapy

CREATE TABLE IF NOT EXISTS therapy
(
    id                BIGSERIAL PRIMARY KEY,
    type              TEXT                                NOT NULL,
    request           VARCHAR                             NOT NULL,
    status            TEXT                                NOT NULL,
    ref_id            BIGINT                              NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_at  TIMESTAMP                           NULL,
    patient_id        BIGINT                              NOT NULL,
    employee_id       BIGINT                              NULL,
    room_id           BIGINT                              NULL,
    appointment_id    BIGINT                              NULL,
    therapy_result_id BIGINT                              NULL,
    FOREIGN KEY (patient_id) REFERENCES patient (id),
    FOREIGN KEY (employee_id) REFERENCES employee (id),
    FOREIGN KEY (room_id) REFERENCES room (id),
    FOREIGN KEY (appointment_id) REFERENCES appointment (id),
    FOREIGN KEY (therapy_result_id) REFERENCES therapy_result (id)
);