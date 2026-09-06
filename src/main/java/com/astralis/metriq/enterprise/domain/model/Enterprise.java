package com.astralis.metriq.enterprise.domain.model;

import java.sql.Date;
import java.util.UUID;

import org.hibernate.validator.constraints.br.CNPJ;

import com.astralis.metriq.enterprise.domain.enums.PlanType;
import com.astralis.metriq.enterprise.domain.enums.SubscriptionStatus;

import jakarta.validation.constraints.Email;
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

  private String razao_social;

  @CNPJ
  private String cnpj;

  @Email
  private String email;

  private String telefone;

  private SubscriptionStatus status;

  private PlanType plano;

  private Date created_at;

  private Date updated_at;

}
