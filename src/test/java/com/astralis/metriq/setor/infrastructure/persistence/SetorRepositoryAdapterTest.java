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
    SetorEntity found = adapter.findById(saved.getId());
    assertEquals(enterprise.getId(), found.getEnterpriseId());
    assertEquals("Laboratorio", found.getName());
    assertEquals("Calibracao", found.getDescricao());
    assertTrue(found.getAtivo());
    assertEquals(now, found.getCreatedAt());
    assertEquals(now, found.getUpdatedAt());
    assertEquals(saved.getId(), adapter.findByEnterpriseId(enterprise.getId()).getId());
    assertEquals(saved.getId(), adapter.findByName("Laboratorio", enterprise.getId()).getId());
    assertEquals(new SetorResponse(saved.getId(), enterprise.getId(), "Laboratorio",
        "Calibracao", true, now, now), SetorResponse.from(found));

    found.setName("Novo nome");
    found.setAtivo(false);
    adapter.save(found);
    entityManager.flush();
    entityManager.clear();
    assertEquals(saved.getId(), adapter.findByName("Novo nome", enterprise.getId()).getId());
    assertFalse(adapter.findById(saved.getId()).getAtivo());

    adapter.deleteById(saved.getId());
    entityManager.flush();
    entityManager.clear();
    assertThrows(SetorNotFoundException.class, () -> adapter.findById(saved.getId()));
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

    assertEquals(firstSetor.getId(), adapter.findByName("Laboratorio", first.getId()).getId());
    assertEquals(secondSetor.getId(), adapter.findByName("Laboratorio", second.getId()).getId());
    assertThrows(SetorNotFoundException.class,
        () -> adapter.findByName("Exclusivo", second.getId()));
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
  void shouldThrowWhenSearchHasNoResult() {
    assertThrows(SetorNotFoundException.class, () -> adapter.findById(UUID.randomUUID()));
    assertThrows(SetorNotFoundException.class, () -> adapter.findByEnterpriseId(UUID.randomUUID()));
    assertThrows(SetorNotFoundException.class, () -> adapter.findByName("Inexistente", UUID.randomUUID()));
  }
}
