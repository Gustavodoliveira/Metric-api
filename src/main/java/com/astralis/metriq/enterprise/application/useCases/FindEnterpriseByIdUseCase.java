package com.astralis.metriq.enterprise.application.useCases;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.astralis.metriq.enterprise.domain.model.Enterprise;
import com.astralis.metriq.enterprise.domain.repositories.EnterpriseRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FindEnterpriseByIdUseCase {

  private final EnterpriseRepository repository;

  public Optional<Enterprise> executeFindEnterpriseById(UUID id) {
    return repository.findById(id);
  }

}
