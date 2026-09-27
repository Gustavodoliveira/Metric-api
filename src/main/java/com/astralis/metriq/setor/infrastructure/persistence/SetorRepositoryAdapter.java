package com.astralis.metriq.setor.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.astralis.metriq.setor.domain.model.SetorEntity;
import com.astralis.metriq.setor.domain.repositories.SetorRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SetorRepositoryAdapter implements SetorRepository {

  private final PostgresDataSetorRepository repository;
  private final SetorMapper mapper;

  @Override
  public SetorEntity save(SetorEntity entity) {
    return mapper.toDomain(repository.save(mapper.toEntity(entity)));
  }

  @Override
  public List<SetorEntity> findByEnterpriseId(UUID id) {
    return repository.findByEnterpriseId(id).stream().map(mapper::toDomain).toList();
  }

  @Override
  public Optional<SetorEntity> findById(UUID id) {
    return repository.findById(id).map(mapper::toDomain);
  }

  @Override
  public Optional<SetorEntity> findByName(String name, UUID enterpriseId) {
    return repository.findByNomeAndEnterpriseId(name, enterpriseId).map(mapper::toDomain);
  }

  @Override
  public Optional<SetorEntity> findByIdAndEnterpriseId(UUID id, UUID enterpriseId) {
    return repository.findByIdAndEnterprise_Id(id, enterpriseId).map(mapper::toDomain);
  }

  @Override
  public void deleteById(UUID id) {
    repository.deleteById(id);
  }
}
