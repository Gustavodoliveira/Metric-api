package com.astralis.metriq.setor.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.astralis.metriq.setor.domain.model.SetorEntity;

public interface SetorRepository {

  SetorEntity save(SetorEntity entity);

  List<SetorEntity> findByEnterpriseId(UUID id);

  Optional<SetorEntity> findById(UUID id);

  Optional<SetorEntity> findByIdAndEnterpriseId(UUID id, UUID enterpriseId);

  Optional<SetorEntity> findByName(String name, UUID enterpriseId);

  void deleteById(UUID id);
}
