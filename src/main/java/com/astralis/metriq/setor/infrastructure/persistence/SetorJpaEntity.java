package com.astralis.metriq.setor.infrastructure.persistence;

import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;
import java.util.UUID;

import com.astralis.metriq.enterprise.infrastructure.persistence.EnterpriseJpaEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "setor", uniqueConstraints = @UniqueConstraint(
    name = "uk_setor_enterprise_nome", columnNames = {"enterprise_id", "nome"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SetorJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "enterprise_id", nullable = false)
  private EnterpriseJpaEntity enterprise;

  @Column(name = "nome", nullable = false, length = 100)
  private String nome;

  @Column(name = "descricao", length = 255)
  private String descricao;

  @Column(name = "ativo", nullable = false)
  private Boolean ativo;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
}
