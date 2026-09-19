package com.astralis.metriq.users.domain.model;

import java.sql.Date;
import java.util.UUID;

import com.astralis.metriq.users.domain.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

  private UUID id;

  private UUID empresa_id;

  private String name;

  private String email;

  private String senha;

  private String perfil;

  private Status status;

  private Date createdAt;

  private Date updateAt;

}
