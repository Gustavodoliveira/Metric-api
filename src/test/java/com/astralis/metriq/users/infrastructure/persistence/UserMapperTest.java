package com.astralis.metriq.users.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.astralis.metriq.users.application.dtos.CreateUserRequest;
import com.astralis.metriq.users.application.mapper.CreateUserMapper;
import com.astralis.metriq.users.domain.enums.Status;
import com.astralis.metriq.users.domain.model.UserEntity;

class UserMapperTest {
  private final UserMapper mapper = new UserMapper();
  private final CreateUserMapper requestMapper = new CreateUserMapper();

  @ParameterizedTest
  @EnumSource(Status.class)
  void preservesUserDataAcrossRequestDomainAndPersistence(Status status) {
    UUID enterpriseId = UUID.randomUUID();
    CreateUserRequest request = new CreateUserRequest(enterpriseId, "Ana", "ana@example.com",
        "senha", "ADMIN", status);
    UserEntity domain = requestMapper.toDomain(request);

    assertThat(domain.getId()).isNull();
    assertThat(domain.getEmpresa_id()).isEqualTo(enterpriseId);
    assertThat(domain.getName()).isEqualTo(request.name());
    assertThat(domain.getEmail()).isEqualTo(request.email());
    assertThat(domain.getSenha()).isEqualTo(request.senha());
    assertThat(domain.getPerfil()).isEqualTo(request.perfil());
    assertThat(domain.getStatus()).isEqualTo(status);
    assertThat(domain.getCreatedAt()).isNotNull();
    assertThat(domain.getUpdateAt()).isEqualTo(domain.getCreatedAt());
    domain.setId(UUID.randomUUID());

    UserJpaEntity entity = mapper.toEntity(domain);
    assertThat(entity.getEnterprise().getId()).isEqualTo(enterpriseId);
    assertThat(entity.getStatus()).isEqualTo(status);
    assertThat(mapper.toDomain(entity)).usingRecursiveComparison().isEqualTo(domain);
  }

  @Test
  void preservesOptionalEnterprise() {
    UserEntity domain = new UserEntity();
    assertThat(mapper.toEntity(domain).getEnterprise()).isNull();
    assertThat(mapper.toDomain(mapper.toEntity(domain)).getEmpresa_id()).isNull();
  }

  @Test
  void handlesNullInputs() {
    assertThat(mapper.toEntity(null)).isNull();
    assertThat(mapper.toDomain(null)).isNull();
    assertThat(requestMapper.toDomain(null)).isNull();
  }
}
