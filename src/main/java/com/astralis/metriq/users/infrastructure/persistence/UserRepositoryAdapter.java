package com.astralis.metriq.users.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.astralis.metriq.users.domain.exceptions.UserNotFoundException;
import com.astralis.metriq.users.domain.model.UserEntity;
import com.astralis.metriq.users.domain.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

  private final PostgresDataUsersRepository repository;

  private final UserMapper mapper;

  @Override
  public UserEntity createUser(UserEntity user) {
    UserJpaEntity entity = mapper.toEntity(user);
    repository.save(entity);
    return mapper.toDomain(entity);
  }

  @Override
  public Optional<UserEntity> findByEmail(String email) {
    return repository.findByEmail(email).map((s) -> mapper.toDomain(s));
  }

  @Override
  public Optional<UserEntity> findById(UUID id) {
    return repository.findById(id).map((s) -> mapper.toDomain(s));
  }

  @Override
  public Optional<UserEntity> findByEnterpriseId(UUID enterpriseId) {
    return repository.findByEnterprise_id(enterpriseId).map((s) -> mapper.toDomain(s));
  }

  @Override
  public void deleteUser(UUID id, UUID enterpriseId) {
    var user = repository.findById(id).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

    if (!user.getEnterprise().getId().equals(enterpriseId)) {
      throw new UserNotFoundException("Usuário não encontrado");
    }
    return;
  }

}
