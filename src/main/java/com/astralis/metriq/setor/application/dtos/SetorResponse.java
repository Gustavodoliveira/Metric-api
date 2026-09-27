package com.astralis.metriq.setor.application.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import com.astralis.metriq.setor.domain.model.SetorEntity;

public record SetorResponse(UUID id, UUID enterpriseId, String name, String descricao,
    Boolean ativo, LocalDateTime createdAt, LocalDateTime updatedAt) {

  public static SetorResponse from(SetorEntity setor) {
    return new SetorResponse(setor.getId(), setor.getEnterpriseId(), setor.getName(),
        setor.getDescricao(), setor.getAtivo(), setor.getCreatedAt(), setor.getUpdatedAt());
  }
}
