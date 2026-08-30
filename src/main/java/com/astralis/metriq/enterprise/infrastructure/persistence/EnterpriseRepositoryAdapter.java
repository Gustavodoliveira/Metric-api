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

  @Override
  public Enterprise save(Enterprise enterprise) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'save'");
  }

  @Override
  public Optional<Enterprise> findById(UUID id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findById'");
  }

  @Override
  public Boolean existsByCnpjAndEnterpriseId(String Cnpj, UUID enterpriseIs) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'existsByCnpjAndEnterpriseId'");
  }

  @Override
  public void deleteById(UUID enterpriseId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
  }

}
