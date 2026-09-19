package com.astralis.metriq.enterprise.application.usecase;

import org.springframework.stereotype.Service;

import com.astralis.metriq.enterprise.application.dto.CreateEnterpriseRequest;
import com.astralis.metriq.enterprise.domain.model.Enterprise;
import com.astralis.metriq.enterprise.domain.repositories.EnterpriseRepository;
import com.astralis.metriq.enterprise.application.mapper.CreateEnterpriseMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CreateEnterpriseUseCase {

  private final CreateEnterpriseMapper mapper;

  private final EnterpriseRepository repository;

  public Enterprise execute(CreateEnterpriseRequest enterpriseRequest) {
    Enterprise enterprise = mapper.toDomain(enterpriseRequest);
    Enterprise entity = repository.save(enterprise);

    return entity;
  }

}
