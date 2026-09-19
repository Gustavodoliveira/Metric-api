package com.astralis.metriq.users.application.mapper;

import java.sql.Date;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.astralis.metriq.users.application.dtos.CreateUserRequest;
import com.astralis.metriq.users.domain.model.UserEntity;

@Component
public class CreateUserMapper {

  public UserEntity toDomain(CreateUserRequest request) {
    if (request == null) {
      return null;
    }

    LocalDateTime now = LocalDateTime.now();
    return new UserEntity(null, request.enterpriseId(), request.name(), request.email(),
        request.senha(), request.perfil(), request.status(), now, now);
  }
}
