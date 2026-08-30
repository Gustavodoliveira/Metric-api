package com.astralis.metriq.enterprise.application.useCase;

import org.springframework.stereotype.Service;

import com.astralis.metriq.enterprise.application.Dtos.CreateEnterpriseRequest;
import com.astralis.metriq.enterprise.domain.model.Enterprise;
import com.astralis.metriq.enterprise.domain.repositories.EnterpriseRepository;
import com.astralis.metriq.enterprise.infrastructure.persistence.EnterpriseMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CreateEnterpriseUseCase {

  private final EnterpriseMapper mapper;

  private final EnterpriseRepository repository;

  public Enterprise execute(CreateEnterpriseRequest enterpriseRequest) {
    Enterprise enterprise = mapper.toDomain(enterpriseRequest);
    Enterprise entity = repository.save(enterprise);

    return entity;
  }

}
