package com.astralis.metriq.setor.application.useCases;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;

import com.astralis.metriq.setor.domain.exceptions.SetorAlreadyExistsException;
import com.astralis.metriq.enterprise.domain.exceptions.EnterpriseNotFoundException;
import com.astralis.metriq.enterprise.domain.repositories.EnterpriseRepository;
import com.astralis.metriq.setor.application.dtos.CreateSetorRequestDto;
import com.astralis.metriq.setor.application.mapper.CreateSetorMapper;
import com.astralis.metriq.setor.domain.model.SetorEntity;
import com.astralis.metriq.setor.domain.repositories.SetorRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CreateSetorUseCase {

  private final EnterpriseRepository enterpriseRepository;

  private final SetorRepository repository;

  private final CreateSetorMapper createSetorMapper;

  @Transactional
  public SetorEntity execute(CreateSetorRequestDto request) {
    enterpriseRepository.findById(request.enterpriseId()).orElseThrow(() -> new EnterpriseNotFoundException());
    if (repository.findByName(request.name(), request.enterpriseId()).isPresent()) {
      throw new SetorAlreadyExistsException();
    }
    SetorEntity entity = repository.save(createSetorMapper.toDomain(request));
    return entity;
  }
}
