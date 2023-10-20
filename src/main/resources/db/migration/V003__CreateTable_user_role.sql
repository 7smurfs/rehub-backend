-- Create table user_role

CREATE TABLE IF NOT EXISTS user_role
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES rehub_user (id),
    FOREIGN KEY (role_id) REFERENCES role (id)
);