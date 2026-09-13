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
public class FindEnterpriseByCnpjUseCase {

  private final EnterpriseMapper mapper;

  private final EnterpriseRepository repository;

  public Optional<Enterprise> executeFindEnterpriseByCnpj(String cnpj) {
    return repository.findByCnpj(formatCnpj(cnpj));

  }

  private String formatCnpj(String cnpj) {
    if (cnpj == null || !cnpj.matches("[0-9]{14}")) {
      throw new IllegalArgumentException("O CNPJ deve conter exatamente 14 números.");
    }

    return cnpj.substring(0, 2) + "."
        + cnpj.substring(2, 5) + "."
        + cnpj.substring(5, 8) + "/"
        + cnpj.substring(8, 12) + "-"
        + cnpj.substring(12, 14);
  }
}
