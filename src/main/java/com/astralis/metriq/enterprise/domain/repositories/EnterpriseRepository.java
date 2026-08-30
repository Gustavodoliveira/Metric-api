package com.astralis.metriq.enterprise.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import com.astralis.metriq.enterprise.domain.model.Enterprise;

public interface EnterpriseRepository {

  Enterprise save(Enterprise enterprise);

  Optional<Enterprise> findById(UUID id);

  Boolean existsByCnpjAndEnterpriseId(String Cnpj, UUID enterpriseIs);

  void deleteById(UUID enterpriseId);
}
