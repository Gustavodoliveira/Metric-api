package com.astralis.metriq.enterprise.infrastructure.persistence;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "enterprise")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnterpriseJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "razao_social", nullable = false)
  private String razao_social;

  @Column(name = "cnpj", nullable = false)
  private String cnpj;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "telefone", nullable = false)
  private String telefone;

  @Column(name = "created_at", nullable = false)
  private Date created_at;

  @Column(name = "updated_at", nullable = false)
  private Date updated_at;
}
