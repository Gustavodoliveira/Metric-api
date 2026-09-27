package com.astralis.metriq.enterprise.application.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.astralis.metriq.enterprise.application.dtos.CreateEnterpriseRequest;
import com.astralis.metriq.enterprise.domain.model.Enterprise;

@Component
public class CreateEnterpriseMapper {
  public Enterprise toDomain(CreateEnterpriseRequest request) {
    if (request == null) {
      return null;
    }

    LocalDateTime now = LocalDateTime.now();
    return new Enterprise(null, request.razaoSocial(), request.cnpj(), request.email(),
        request.telefone(), request.status(), request.plano(), now, now);
  }
}
