-- Active: 1770497267225@@127.0.0.1@5432@metriq
CREATE TABLE enterprise (
  id UUID primary key,
  razao_social VARCHAR(255) not null,
  cnpj VARCHAR(255) not null,
  email VARCHAR(255) not null,
  telefone VARCHAR(255) not null,
  status VARCHAR(255) not null,
  plano VARCHAR(255) not null,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
)