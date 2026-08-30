package com.astralis.metriq.enterprise.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.astralis.metriq.enterprise.domain.model.Enterprise;
import com.astralis.metriq.enterprise.domain.repositories.EnterpriseRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EnterpriseRepositoryAdapter implements EnterpriseRepository {

  private final PostgresDataEnterpriseRepository repository;
  private final EnterpriseMapper mapper;

  @Override
  public Enterprise save(Enterprise enterprise) {
    EnterpriseJpaEntity entity = mapper.toEntity(enterprise);
    EnterpriseJpaEntity savedEntity = repository.save(entity);
    return mapper.toDomain(savedEntity);
  }

  @Override
  public Optional<Enterprise> findById(UUID id) {
    return repository.findById(id).map(mapper::toDomain);
  }

  @Override
  public Boolean existsByCnpjAndEnterpriseId(String Cnpj, UUID enterpriseIs) {
    EnterpriseJpaEntity exist = repository.findByCnpj(Cnpj);
    if (exist == null) {
      return false;
    } else {
      return true;
    }

  }

  @Override
  public void deleteById(UUID enterpriseId) {
    repository.deleteById(enterpriseId);
    return;
  }

}
