ALTER TABLE therapy
    ADD therapy_scan TEXT;

ALTER TABLE therapy
    ADD CONSTRAINT uc_therapy_scan_url UNIQUE (therapy_scan);