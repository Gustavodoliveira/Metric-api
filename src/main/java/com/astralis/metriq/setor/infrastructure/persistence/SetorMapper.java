package com.astralis.metriq.setor.infrastructure.persistence;

import org.springframework.stereotype.Component;

import com.astralis.metriq.enterprise.infrastructure.persistence.EnterpriseJpaEntity;
import com.astralis.metriq.setor.domain.model.SetorEntity;

@Component
public class SetorMapper {

  public SetorJpaEntity toEntity(SetorEntity setor) {
    if (setor == null) {
      return null;
    }

    EnterpriseJpaEntity enterprise = null;
    if (setor.getEnterpriseId() != null) {
      enterprise = new EnterpriseJpaEntity();
      enterprise.setId(setor.getEnterpriseId());
    }

    return new SetorJpaEntity(setor.getId(), enterprise, setor.getName(),
        setor.getDescricao(), setor.getAtivo(), setor.getCreatedAt(), setor.getUpdatedAt());
  }

  public SetorEntity toDomain(SetorJpaEntity entity) {
    if (entity == null) {
      return null;
    }

    return new SetorEntity(entity.getId(),
        entity.getEnterprise_id() == null ? null : entity.getEnterprise_id().getId(),
        entity.getNome(), entity.getDescricao(), entity.getAtivo(),
        entity.getCreatedAt(), entity.getUpdatedAt());
  }
}
