package com.astralis.metriq.users.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import com.astralis.metriq.users.domain.model.UserEntity;

public interface UserRepository {

  UserEntity createUser(UserEntity user);

  Optional<UserEntity> findByEmail(String email);

  Optional<UserEntity> findById(UUID id);

  Optional<UserEntity> findByEnterpriseId(UUID enterpriseId);

  void deleteUser(UUID id);
}
