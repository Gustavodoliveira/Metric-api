package com.astralis.metriq.users.infrastructure.persistence;

import org.springframework.stereotype.Component;

import com.astralis.metriq.enterprise.infrastructure.persistence.EnterpriseJpaEntity;
import com.astralis.metriq.users.domain.model.UserEntity;

@Component
public class UserMapper {

  public UserJpaEntity toEntity(UserEntity user) {
    if (user == null) {
      return null;
    }

    EnterpriseJpaEntity enterprise = null;
    if (user.getEmpresa_id() != null) {
      enterprise = new EnterpriseJpaEntity();
      enterprise.setId(user.getEmpresa_id());
    }

    return new UserJpaEntity(user.getId(), enterprise, user.getName(), user.getEmail(),
        user.getSenha(), user.getPerfil(), user.getStatus(), user.getCreatedAt(), user.getUpdateAt());
  }

  public UserEntity toDomain(UserJpaEntity entity) {
    if (entity == null) {
      return null;
    }

    return new UserEntity(entity.getId(),
        entity.getEnterprise() == null ? null : entity.getEnterprise().getId(),
        entity.getName(), entity.getEmail(), entity.getSenha(), entity.getPerfil(),
        entity.getStatus(), entity.getCreatedAt(), entity.getUpdateAt());
  }
}
