package com.astralis.metriq.enterprise.application.useCases;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.astralis.metriq.enterprise.domain.repositories.EnterpriseRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DeleteEnterpriseByIdUseCase {

  private final EnterpriseRepository enterpriseRepository;

  public void executeDeleteEnterpriseById(UUID id) {
    enterpriseRepository.deleteById(id);
  }
}
