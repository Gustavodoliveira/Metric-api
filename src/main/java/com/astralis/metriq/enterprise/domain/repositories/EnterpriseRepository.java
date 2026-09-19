package com.astralis.metriq.enterprise.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import com.astralis.metriq.enterprise.domain.model.Enterprise;

public interface EnterpriseRepository {

  Enterprise save(Enterprise enterprise);

  Optional<Enterprise> findById(UUID id);

  Optional<Enterprise> findByCnpj(String cnpj);

  boolean existsByCnpjAndEnterpriseId(String cnpj, UUID enterpriseId);

  void deleteById(UUID enterpriseId);
}
