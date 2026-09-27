ALTER TABLE setor
    ADD CONSTRAINT uk_setor_enterprise_nome UNIQUE (enterprise_id, nome);
