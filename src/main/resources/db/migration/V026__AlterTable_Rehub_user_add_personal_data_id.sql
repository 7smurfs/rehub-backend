ALTER TABLE rehub_user ADD COLUMN personal_data_id TEXT;

ALTER TABLE rehub_user
    ADD CONSTRAINT uc_rehub_personal_data_id UNIQUE (personal_data_id);