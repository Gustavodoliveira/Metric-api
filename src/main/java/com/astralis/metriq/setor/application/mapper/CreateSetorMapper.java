package com.astralis.metriq.setor.application.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.astralis.metriq.setor.application.dtos.CreateSetorRequestDto;
import com.astralis.metriq.setor.domain.model.SetorEntity;

@Component
public class CreateSetorMapper {

  public SetorEntity toDomain(CreateSetorRequestDto request) {
    if (request == null) {
      return null;
    }

    LocalDateTime now = LocalDateTime.now();
    return new SetorEntity(null, request.enterpriseId(), request.name(),
        request.descricao(), request.ativo(), now, now);
  }
}
