package com.astralis.metriq.setor.domain.repositories;

import java.util.UUID;

import com.astralis.metriq.setor.domain.model.SetorEntity;

public interface SetorRepository {

  SetorEntity save(SetorEntity entity);

  SetorEntity findByEnterpriseId(UUID id);

  SetorEntity findById(UUID id);

  SetorEntity findByName(String name, UUID enterpriseId);

  void deleteById(UUID id);
}
