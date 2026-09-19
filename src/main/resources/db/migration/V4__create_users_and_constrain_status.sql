-- The legacy v1_create_table_users.sql name is not recognized by Flyway.
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    empresa_id UUID CONSTRAINT fk_usuario_enterprise REFERENCES enterprise(id),
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE users
    ALTER COLUMN status SET NOT NULL,
    ADD CONSTRAINT chk_users_status CHECK (status IN ('ACTIVE', 'INACTIVE'));
