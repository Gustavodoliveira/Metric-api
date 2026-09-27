CREATE TABLE IF NOT EXISTS setor (
  id UUID PRIMARY KEY, 
  enterprise_id UUID REFERENCES enterprise(id) NOT NULL,
  nome VARCHAR(100) NOT NULL,
  descricao VARCHAR(255),
  ativo BOOLEAN NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
)