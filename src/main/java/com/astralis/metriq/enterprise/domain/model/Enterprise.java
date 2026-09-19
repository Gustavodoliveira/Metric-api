package com.astralis.metriq.enterprise.domain.model;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

import com.astralis.metriq.enterprise.domain.enums.PlanType;
import com.astralis.metriq.enterprise.domain.enums.SubscriptionStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Enterprise {

  private UUID id;

  private String razaoSocial;

  private String cnpj;

  private String email;

  private String telefone;

  private SubscriptionStatus status;

  private PlanType plano;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

}
