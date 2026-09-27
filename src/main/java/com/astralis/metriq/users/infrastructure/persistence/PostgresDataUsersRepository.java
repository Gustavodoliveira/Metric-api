package com.astralis.metriq.users.infrastructure.persistence;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostgresDataUsersRepository extends JpaRepository<UserJpaEntity, UUID> {

  Optional<UserJpaEntity> findByEmail(String email);

  List<UserJpaEntity> findByEnterprise_id(UUID enterpriseId);
}
