CREATE TABLE users (
  id UUID primary key,
  empresa_id UUID CONSTRAINT fk_usuario_enterprise  REFERENCES enterprise(id),
  nome VARCHAR(255) not null,
  email VARCHAR(255) not null,
  senha VARCHAR(255) not null,
  perfil VARCHAR(255) not null,
  status VARCHAR(255) not null,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
)