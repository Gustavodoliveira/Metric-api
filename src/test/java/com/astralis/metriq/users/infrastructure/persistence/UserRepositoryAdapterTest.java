package com.astralis.metriq.users.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.astralis.metriq.enterprise.domain.enums.PlanType;
import com.astralis.metriq.enterprise.domain.enums.SubscriptionStatus;
import com.astralis.metriq.enterprise.domain.model.Enterprise;
import com.astralis.metriq.enterprise.domain.repositories.EnterpriseRepository;
import com.astralis.metriq.users.domain.enums.Status;
import com.astralis.metriq.users.domain.exceptions.UserNotFoundException;
import com.astralis.metriq.users.domain.model.UserEntity;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class UserRepositoryAdapterTest {

  @Autowired
  private EnterpriseRepository enterpriseRepository;

  @Autowired
  private UserRepositoryAdapter adapter;

  private Enterprise enterprise;

  @BeforeEach
  void setup() {
    enterprise = new Enterprise();

    enterprise.setRazaoSocial("Astralis Systems");
    enterprise.setCnpj("47.891.201/0001-90");
    enterprise.setEmail("contato@astralis.com");
    enterprise.setTelefone("11999999999");

    enterprise.setStatus(SubscriptionStatus.ACTIVE);
    enterprise.setPlano(PlanType.PRO);

    LocalDateTime now = LocalDateTime.now();

    enterprise.setCreatedAt(now);
    enterprise.setUpdatedAt(now);

    enterprise = enterpriseRepository.save(enterprise);
  }

  @Test
  void shouldCreateUser() {
    // Arrange
    UserEntity user = new UserEntity();

    user.setName("Gustavo");
    user.setEmail("gustavo@astralis.com");
    user.setEmpresa_id(enterprise.getId());
    user.setSenha("senha-teste");
    user.setPerfil("ADMIN");
    user.setStatus(Status.ACTIVE);
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdateAt(LocalDateTime.now());

    // Act
    UserEntity createdUser = adapter.createUser(user);

    // Assert
    assertNotNull(createdUser);
    assertNotNull(createdUser.getId());

    var userFound = adapter.findById(createdUser.getId());

    assertTrue(userFound.isPresent());

    assertEquals(
        createdUser.getEmail(),
        userFound.get().getEmail());

    assertEquals(
        enterprise.getId(),
        userFound.get().getEmpresa_id());
  }

  @Test
  void shouldDeleteUser() {
    UserEntity user = createUser("gustavo@astralis.com");

    adapter.deleteUser(user.getId(), enterprise.getId());

    assertTrue(adapter.findById(user.getId()).isEmpty());
    assertTrue(adapter.findByEmail(user.getEmail()).isEmpty());
  }

  @Test
  void shouldNotDeleteUserFromAnotherEnterprise() {
    UserEntity user = createUser("gustavo@astralis.com");

    assertThrows(UserNotFoundException.class,
        () -> adapter.deleteUser(user.getId(), UUID.randomUUID()));

    assertTrue(adapter.findById(user.getId()).isPresent());
  }

  @Test
  void shouldThrowWhenDeletingNonexistentUser() {
    assertThrows(UserNotFoundException.class,
        () -> adapter.deleteUser(UUID.randomUUID(), enterprise.getId()));
  }

  @Test
  void shouldFindUserByEmail() {
    UserEntity user = createUser("gustavo@astralis.com");
    createUser("outro@astralis.com");

    UserEntity found = adapter.findByEmail(user.getEmail()).orElseThrow();

    assertEquals(user.getId(), found.getId());
    assertEquals(user.getEmail(), found.getEmail());
    assertEquals(enterprise.getId(), found.getEmpresa_id());
  }

  @Test
  void shouldReturnEmptyWhenEmailDoesNotExist() {
    createUser("gustavo@astralis.com");

    assertTrue(adapter.findByEmail("inexistente@astralis.com").isEmpty());
  }

  @Test
  void shouldFindUsersByEnterpriseId() {
    UserEntity user = createUser("gustavo@astralis.com");

    UserEntity found = adapter.findByEnterpriseId(enterprise.getId()).orElseThrow();

    assertEquals(user.getId(), found.getId());
    assertEquals(user.getEmail(), found.getEmail());
    assertEquals(enterprise.getId(), found.getEmpresa_id());
  }

  @Test
  void shouldReturnEmptyWhenEnterpriseDoesNotExist() {
    createUser("gustavo@astralis.com");

    assertTrue(adapter.findByEnterpriseId(UUID.randomUUID()).isEmpty());
  }

  @Test
  void shouldFindUserById() {
    UserEntity user = new UserEntity();

    user.setName("Gustavo");
    user.setEmail("gustavo@astralis.com");
    user.setEmpresa_id(enterprise.getId());
    user.setSenha("senha-teste");
    user.setPerfil("ADMIN");
    user.setStatus(Status.ACTIVE);
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdateAt(LocalDateTime.now());

    UserEntity sx = adapter.createUser(user);

    Optional<UserEntity> exist = adapter.findById(sx.getId());

    assertTrue(exist.isPresent());
  }

  @Test
  void shouldReturnEmptyWhenUserIdDoesNotExist() {
    createUser("gustavo@astralis.com");

    assertTrue(adapter.findById(UUID.randomUUID()).isEmpty());
  }

  private UserEntity createUser(String email) {
    UserEntity user = new UserEntity();
    user.setName("Gustavo");
    user.setEmail(email);
    user.setEmpresa_id(enterprise.getId());
    user.setSenha("senha-teste");
    user.setPerfil("ADMIN");
    user.setStatus(Status.ACTIVE);
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdateAt(LocalDateTime.now());
    return adapter.createUser(user);
  }
}
