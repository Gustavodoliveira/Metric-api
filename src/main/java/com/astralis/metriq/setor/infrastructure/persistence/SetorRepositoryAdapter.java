package com.astralis.metriq.setor.infrastructure.persistence;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.astralis.metriq.setor.domain.exceptions.SetorNotFoundException;
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
  public SetorEntity findByEnterpriseId(UUID id) {
    return repository.findByEnterpriseId(id).map(mapper::toDomain)
        .orElseThrow(() -> new SetorNotFoundException("Setor não encontrado para a empresa"));
  }

  @Override
  public SetorEntity findById(UUID id) {
    return repository.findById(id).map(mapper::toDomain)
        .orElseThrow(() -> new SetorNotFoundException(id));
  }

  @Override
  public SetorEntity findByName(String name, UUID enterpriseId) {
    return repository.findByNomeAndEnterpriseId(name, enterpriseId).map(mapper::toDomain)
        .orElseThrow(() -> new SetorNotFoundException("Setor não encontrado pelo nome"));
  }

  @Override
  public void deleteById(UUID id) {
    repository.deleteById(id);
  }
}
