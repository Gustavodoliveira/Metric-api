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
        enterprise.getRazaoSocial(),
        enterprise.getCnpj(),
        enterprise.getEmail(),
        enterprise.getTelefone(),
        enterprise.getStatus(),
        enterprise.getPlano(),
        enterprise.getCreatedAt(),
        enterprise.getUpdatedAt());
  }

  public Enterprise toDomain(EnterpriseJpaEntity entity) {
    if (entity == null) {
      return null;
    }

    return new Enterprise(
        entity.getId(),
        entity.getRazaoSocial(),
        entity.getCnpj(),
        entity.getEmail(),
        entity.getTelefone(),
        entity.getStatus(),
        entity.getPlano(),
        entity.getCreatedAt(),
        entity.getUpdatedAt());
  }
}
