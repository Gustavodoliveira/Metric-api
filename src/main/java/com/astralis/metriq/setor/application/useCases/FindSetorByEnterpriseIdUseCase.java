package com.astralis.metriq.setor.application.useCases;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.astralis.metriq.enterprise.domain.exceptions.EnterpriseNotFoundException;
import com.astralis.metriq.enterprise.domain.repositories.EnterpriseRepository;
import com.astralis.metriq.setor.domain.model.SetorEntity;
import com.astralis.metriq.setor.domain.repositories.SetorRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FindSetorByEnterpriseIdUseCase {

  private final EnterpriseRepository enterpriseRepository;

  private final SetorRepository repository;

  public List<SetorEntity> execute(UUID enterpriseId) {
    enterpriseRepository.findById(enterpriseId).orElseThrow(EnterpriseNotFoundException::new);
    return repository.findByEnterpriseId(enterpriseId);
  }
}
