package com.astralis.metriq.enterprise.infrastructure.persistence;

import org.springframework.stereotype.Component;

import com.astralis.metriq.enterprise.domain.model.Enterprise;

@Component
public class EnterpriseMapper {

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
        entity.getCreated_at(),
        entity.getUpdated_at());
  }
}
