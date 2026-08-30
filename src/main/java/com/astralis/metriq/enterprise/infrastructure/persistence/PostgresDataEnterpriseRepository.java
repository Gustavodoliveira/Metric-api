package com.astralis.metriq.enterprise.infrastructure.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostgresDataEnterpriseRepository extends JpaRepository<EnterpriseJpaEntity, UUID> {
  public EnterpriseJpaEntity findByCnpj(String cnpj);
}
