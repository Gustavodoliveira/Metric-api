package com.astralis.metriq.enterprise.application.useCase;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.astralis.metriq.enterprise.domain.model.Enterprise;
import com.astralis.metriq.enterprise.domain.repositories.EnterpriseRepository;
import com.astralis.metriq.enterprise.infrastructure.persistence.EnterpriseJpaEntity;
import com.astralis.metriq.enterprise.infrastructure.persistence.EnterpriseMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FindEnterpriseByCnpj {

  private final EnterpriseMapper mapper;

  private final EnterpriseRepository repository;

  public Optional<Enterprise> executeFindEnterpriseByCnpj(String cnpj) {
    return repository.findByCnpj(cnpj);

  }
}
