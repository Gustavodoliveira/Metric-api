package com.astralis.metriq.users.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.astralis.metriq.enterprise.domain.enums.PlanType;
import com.astralis.metriq.enterprise.domain.enums.SubscriptionStatus;
import com.astralis.metriq.enterprise.domain.model.Enterprise;
import com.astralis.metriq.enterprise.domain.repositories.EnterpriseRepository;
import com.astralis.metriq.users.domain.enums.Status;
import com.astralis.metriq.users.domain.model.UserEntity;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class UserRepositoryAdapterTest {

  @Autowired
  private EnterpriseRepository enterpriseRepository;

  @Autowired
  private UserRepositoryAdapter adapter;

  @Autowired
  private UserMapper mapper;

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
  void testCreateUser() {
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
  void testDeleteUser() {

  }

  @Test
  void testFindByEmail() {

  }

  @Test
  void testFindByEnterpriseId() {

  }

  @Test
  void testFindById() {
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
}
