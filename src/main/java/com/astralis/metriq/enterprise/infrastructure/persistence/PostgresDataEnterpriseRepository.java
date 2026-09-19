package com.astralis.metriq.enterprise.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostgresDataEnterpriseRepository extends JpaRepository<EnterpriseJpaEntity, UUID> {
  boolean existsByCnpjAndId(String cnpj, UUID id);

  Optional<EnterpriseJpaEntity> findByCnpj(String cnpj);
}
