package com.astralis.metriq.setor.application.useCases;

import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import com.astralis.metriq.setor.domain.exceptions.SetorNotFoundException;

import org.springframework.stereotype.Service;

import com.astralis.metriq.setor.domain.repositories.SetorRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DeleteSetorByIdUseCase {

  private final SetorRepository repository;

  @Transactional
  public void execute(UUID id, UUID enterpriseId) {
    repository.findByIdAndEnterpriseId(id, enterpriseId)
        .orElseThrow(() -> new SetorNotFoundException(id));
    repository.deleteById(id);
  }

}
