package com.astralis.metriq.setor.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SetorEntity {

  private UUID id;

  private UUID enterpriseId;

  private String name;

  private String descricao;

  private Boolean ativo;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

}
