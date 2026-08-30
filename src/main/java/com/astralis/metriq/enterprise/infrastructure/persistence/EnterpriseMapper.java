package com.astralis.metriq.enterprise.infrastructure.persistence;

import java.sql.Date;

import org.springframework.stereotype.Component;

import com.astralis.metriq.enterprise.application.Dtos.CreateEnterpriseRequest;
import com.astralis.metriq.enterprise.domain.model.Enterprise;

@Component
public class EnterpriseMapper {

  public Enterprise toDomain(CreateEnterpriseRequest request) {
    if (request == null) {
      return null;
    }

    Date now = new Date(System.currentTimeMillis());

    return new Enterprise(
        null,
        request.razao_social(),
        request.cnpj(),
        request.email(),
        request.telefone(),
        request.status(),
        request.plano(),
        now,
        now);
  }

  public EnterpriseJpaEntity toEntity(Enterprise enterprise) {
    if (enterprise == null) {
      return null;
    }

    return new EnterpriseJpaEntity(
        enterprise.getId(),
        enterprise.getRazao_social(),
        enterprise.getCnpj(),
        enterprise.getEmail(),
        enterprise.getTelefone(),
        enterprise.getStatus(),
        enterprise.getPlano(),
        enterprise.getCreated_at(),
        enterprise.getUpdated_at());
  }

  public Enterprise toDomain(EnterpriseJpaEntity entity) {
    if (entity == null) {
      return null;
    }

    return new Enterprise(
        entity.getId(),
        entity.getRazao_social(),
        entity.getCnpj(),
        entity.getEmail(),
        entity.getTelefone(),
        entity.getStatus(),
        entity.getPlano(),
        entity.getCreated_at(),
        entity.getUpdated_at());
  }
}
