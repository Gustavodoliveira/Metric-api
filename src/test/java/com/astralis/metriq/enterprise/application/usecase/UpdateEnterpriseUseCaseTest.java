package com.astralis.metriq.enterprise.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.astralis.metriq.enterprise.application.dtos.UpdateEnterpriseRequest;
import com.astralis.metriq.enterprise.application.useCases.UpdateEnterpriseUseCase;
import com.astralis.metriq.enterprise.domain.enums.*;
import com.astralis.metriq.enterprise.domain.exceptions.EnterpriseAlreadyExistsException;
import com.astralis.metriq.enterprise.domain.model.Enterprise;
import com.astralis.metriq.enterprise.domain.repositories.EnterpriseRepository;

@ExtendWith(MockitoExtension.class)
class UpdateEnterpriseUseCaseTest {
  @Mock
  private EnterpriseRepository repository;
  private UpdateEnterpriseUseCase useCase;
  private Enterprise enterprise;
  private final UUID id = UUID.randomUUID();
  private final UpdateEnterpriseRequest request = new UpdateEnterpriseRequest("Novo nome",
      "11.222.333/0001-81", "novo@example.com", "11999999999", SubscriptionStatus.ACTIVE, PlanType.PRO);

  @BeforeEach
  void setup() {
    useCase = new UpdateEnterpriseUseCase(repository);
    enterprise = new Enterprise();
    enterprise.setId(id);
    enterprise.setRazaoSocial("Antigo");
    enterprise.setCreatedAt(LocalDateTime.of(2026, 1, 1, 0, 0));
    enterprise.setUpdatedAt(enterprise.getCreatedAt());
  }

  @Test
  void updatesAllFieldsPreservingIdentityAndCreationDate() {
    LocalDateTime createdAt = enterprise.getCreatedAt();
    when(repository.findById(id)).thenReturn(Optional.of(enterprise));
    when(repository.findByCnpj(request.cnpj())).thenReturn(Optional.empty());
    Enterprise saved = new Enterprise();
    when(repository.save(enterprise)).thenReturn(saved);

    assertSame(saved, useCase.execute(id, request).orElseThrow());

    verify(repository).save(enterprise);
    assertAll(
        () -> assertEquals(id, enterprise.getId()),
        () -> assertEquals(createdAt, enterprise.getCreatedAt()),
        () -> assertTrue(enterprise.getUpdatedAt().isAfter(createdAt)),
        () -> assertEquals(request.razaoSocial(), enterprise.getRazaoSocial()),
        () -> assertEquals(request.cnpj(), enterprise.getCnpj()),
        () -> assertEquals(request.email(), enterprise.getEmail()),
        () -> assertEquals(request.telefone(), enterprise.getTelefone()),
        () -> assertEquals(request.status(), enterprise.getStatus()),
        () -> assertEquals(request.plano(), enterprise.getPlano()));
  }

  @Test
  void missingEnterpriseDoesNotSave() {
    when(repository.findById(id)).thenReturn(Optional.empty());
    assertTrue(useCase.execute(id, request).isEmpty());
    verify(repository).findById(id);
    verifyNoMoreInteractions(repository);
  }

  @Test
  void allowsKeepingOwnCnpj() {
    enterprise.setCnpj(request.cnpj());
    when(repository.findById(id)).thenReturn(Optional.of(enterprise));
    when(repository.findByCnpj(request.cnpj())).thenReturn(Optional.of(enterprise));
    when(repository.save(enterprise)).thenReturn(enterprise);
    assertSame(enterprise, useCase.execute(id, request).orElseThrow());
    verify(repository).save(enterprise);
  }

  @Test
  void duplicateCnpjDoesNotModifyOrSaveEnterprise() {
    Enterprise other = new Enterprise();
    other.setId(UUID.randomUUID());
    when(repository.findById(id)).thenReturn(Optional.of(enterprise));
    when(repository.findByCnpj(request.cnpj())).thenReturn(Optional.of(other));
    assertThrows(EnterpriseAlreadyExistsException.class, () -> useCase.execute(id, request));
    verify(repository, never()).save(any());
    assertEquals("Antigo", enterprise.getRazaoSocial());
  }

  @Test
  void propagatesSaveFailure() {
    when(repository.findById(id)).thenReturn(Optional.of(enterprise));
    when(repository.findByCnpj(request.cnpj())).thenReturn(Optional.empty());
    RuntimeException failure = new RuntimeException("Falha na persistência");
    when(repository.save(enterprise)).thenThrow(failure);
    assertSame(failure, assertThrows(RuntimeException.class, () -> useCase.execute(id, request)));
  }
}
