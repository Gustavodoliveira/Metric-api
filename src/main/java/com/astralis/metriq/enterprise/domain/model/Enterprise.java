package com.astralis.metriq.enterprise.domain.model;

import java.sql.Date;
import java.util.UUID;

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

  private String cnpj;

  private String email;

  private String telefone;

  private Date created_at;

  private Date updated_at;

}
