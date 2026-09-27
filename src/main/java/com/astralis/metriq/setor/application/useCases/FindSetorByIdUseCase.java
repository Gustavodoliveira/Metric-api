package com.astralis.metriq.setor.application.useCases;

import java.util.UUID;
import com.astralis.metriq.setor.domain.exceptions.SetorNotFoundException;

import org.springframework.stereotype.Service;

import com.astralis.metriq.setor.domain.model.SetorEntity;
import com.astralis.metriq.setor.domain.repositories.SetorRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FindSetorByIdUseCase {
  private final SetorRepository repository;

  public SetorEntity execute(UUID id, UUID enterpriseId) {
    return repository.findByIdAndEnterpriseId(id, enterpriseId)
        .orElseThrow(() -> new SetorNotFoundException(id));
  }
}
