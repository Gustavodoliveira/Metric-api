package com.astralis.metriq.setor.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.astralis.metriq.enterprise.domain.enums.PlanType;
import com.astralis.metriq.enterprise.domain.enums.SubscriptionStatus;
import com.astralis.metriq.enterprise.domain.model.Enterprise;
import com.astralis.metriq.enterprise.domain.repositories.EnterpriseRepository;
import com.astralis.metriq.setor.application.dtos.SetorResponse;
import com.astralis.metriq.setor.domain.exceptions.SetorNotFoundException;
import com.astralis.metriq.setor.domain.model.SetorEntity;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class SetorRepositoryAdapterTest {

  @Autowired
  private SetorRepositoryAdapter adapter;
  @Autowired
  private EnterpriseRepository enterprises;
  @Autowired
  private EntityManager entityManager;

  @Test
  void shouldPersistFindUpdateAndDeleteSetor() {
    LocalDateTime now = LocalDateTime.now().withNano(0);
    Enterprise enterprise = new Enterprise();
    enterprise.setRazaoSocial("Empresa teste setor");
    enterprise.setCnpj("47.891.201/0001-90");
    enterprise.setEmail("setor@example.com");
    enterprise.setTelefone("11999999999");
    enterprise.setStatus(SubscriptionStatus.ACTIVE);
    enterprise.setPlano(PlanType.PRO);
    enterprise.setCreatedAt(now);
    enterprise.setUpdatedAt(now);
    enterprise = enterprises.save(enterprise);

    SetorEntity saved = adapter.save(new SetorEntity(null, enterprise.getId(),
        "Laboratorio", "Calibracao", true, now, now));
    entityManager.flush();
    entityManager.clear();

    assertNotNull(saved.getId());
    SetorEntity found = adapter.findById(saved.getId()).orElseThrow();
    assertEquals(enterprise.getId(), found.getEnterpriseId());
    assertEquals("Laboratorio", found.getName());
    assertEquals("Calibracao", found.getDescricao());
    assertTrue(found.getAtivo());
    assertEquals(now, found.getCreatedAt());
    assertEquals(now, found.getUpdatedAt());
    assertEquals(saved.getId(), adapter.findByEnterpriseId(enterprise.getId()).getFirst().getId());
    assertEquals(saved.getId(), adapter.findByName("Laboratorio", enterprise.getId()).orElseThrow().getId());
    assertEquals(new SetorResponse(saved.getId(), enterprise.getId(), "Laboratorio",
        "Calibracao", true, now, now), SetorResponse.from(found));

    found.setName("Novo nome");
    found.setAtivo(false);
    adapter.save(found);
    entityManager.flush();
    entityManager.clear();
    assertEquals(saved.getId(), adapter.findByName("Novo nome", enterprise.getId()).orElseThrow().getId());
    assertFalse(adapter.findById(saved.getId()).orElseThrow().getAtivo());

    adapter.deleteById(saved.getId());
    entityManager.flush();
    entityManager.clear();
    assertTrue(adapter.findById(saved.getId()).isEmpty());
  }

  @Test
  void shouldFindSameNameOnlyWithinRequestedEnterprise() {
    LocalDateTime now = LocalDateTime.now().withNano(0);
    Enterprise first = createEnterprise("11.111.111/0001-11", "first@example.com", now);
    Enterprise second = createEnterprise("22.222.222/0001-22", "second@example.com", now);
    SetorEntity firstSetor = adapter.save(new SetorEntity(null, first.getId(),
        "Laboratorio", "Primeira empresa", true, now, now));
    SetorEntity secondSetor = adapter.save(new SetorEntity(null, second.getId(),
        "Laboratorio", "Segunda empresa", true, now, now));
    adapter.save(new SetorEntity(null, first.getId(),
        "Exclusivo", "Somente na primeira empresa", true, now, now));
    entityManager.flush();
    entityManager.clear();

    assertEquals(firstSetor.getId(), adapter.findByName("Laboratorio", first.getId()).orElseThrow().getId());
    assertEquals(secondSetor.getId(), adapter.findByName("Laboratorio", second.getId()).orElseThrow().getId());
    assertTrue(adapter.findByName("Exclusivo", second.getId()).isEmpty());
  }

  @Autowired
  private com.astralis.metriq.setor.application.useCases.CreateSetorUseCase createSetor;
  @Autowired
  private com.astralis.metriq.setor.application.useCases.FindSetorByIdUseCase findSetor;
  @Autowired
  private com.astralis.metriq.setor.application.useCases.DeleteSetorByIdUseCase deleteSetor;

  @Test
  void shouldRejectDuplicateNamesAndIsolateCompanies() {
    LocalDateTime now = LocalDateTime.now();
    Enterprise first = createEnterprise("33.333.333/0001-33", "third@example.com", now);
    Enterprise second = createEnterprise("44.444.444/0001-44", "fourth@example.com", now);
    var request = new com.astralis.metriq.setor.application.dtos.CreateSetorRequestDto(
        first.getId(), "Laboratorio", null, true);
    SetorEntity saved = createSetor.execute(request);
    entityManager.flush();
    entityManager.clear();
    assertEquals(saved.getId(), findSetor.execute(saved.getId(), first.getId()).getId());
    assertThrows(com.astralis.metriq.setor.domain.exceptions.SetorAlreadyExistsException.class,
        () -> createSetor.execute(request));
    assertThrows(SetorNotFoundException.class, () -> findSetor.execute(saved.getId(), second.getId()));
    assertThrows(SetorNotFoundException.class, () -> deleteSetor.execute(saved.getId(), second.getId()));
    assertTrue(adapter.findById(saved.getId()).isPresent());
    deleteSetor.execute(saved.getId(), first.getId());
    assertTrue(adapter.findById(saved.getId()).isEmpty());
  }

  @Test
  void shouldEnforceUniqueNameInDatabase() {
    LocalDateTime now = LocalDateTime.now();
    Enterprise enterprise = createEnterprise("55.555.555/0001-55", "fifth@example.com", now);
    adapter.save(new SetorEntity(null, enterprise.getId(), "Mesmo nome", null, true, now, now));
    entityManager.flush();
    assertThrows(org.hibernate.exception.ConstraintViolationException.class, () -> {
      adapter.save(new SetorEntity(null, enterprise.getId(), "Mesmo nome", null, true, now, now));
      entityManager.flush();
    });
  }

  private Enterprise createEnterprise(String cnpj, String email, LocalDateTime now) {
    Enterprise enterprise = new Enterprise();
    enterprise.setRazaoSocial("Empresa teste setor");
    enterprise.setCnpj(cnpj);
    enterprise.setEmail(email);
    enterprise.setTelefone("11999999999");
    enterprise.setStatus(SubscriptionStatus.ACTIVE);
    enterprise.setPlano(PlanType.PRO);
    enterprise.setCreatedAt(now);
    enterprise.setUpdatedAt(now);
    return enterprises.save(enterprise);
  }

  @Test
  void shouldReturnEmptyWhenSearchHasNoResult() {
    assertTrue(adapter.findById(UUID.randomUUID()).isEmpty());
    assertTrue(adapter.findByEnterpriseId(UUID.randomUUID()).isEmpty());
    assertTrue(adapter.findByName("Inexistente", UUID.randomUUID()).isEmpty());
  }
}
