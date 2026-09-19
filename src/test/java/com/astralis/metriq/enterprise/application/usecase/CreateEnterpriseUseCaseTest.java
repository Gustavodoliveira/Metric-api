package com.astralis.metriq.enterprise.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.astralis.metriq.enterprise.domain.enums.PlanType;
import com.astralis.metriq.enterprise.domain.enums.SubscriptionStatus;
import com.astralis.metriq.enterprise.domain.model.Enterprise;
import com.astralis.metriq.enterprise.domain.repositories.EnterpriseRepository;
import com.astralis.metriq.enterprise.application.dtos.CreateEnterpriseRequest;
import com.astralis.metriq.enterprise.application.mapper.CreateEnterpriseMapper;
import com.astralis.metriq.enterprise.application.useCases.CreateEnterpriseUseCase;

@ExtendWith(MockitoExtension.class)
public class CreateEnterpriseUseCaseTest {

  @Mock
  private EnterpriseRepository repository;

  @Test
  void shouldCreateEnterpriseSuccessfully() {
    CreateEnterpriseRequest request = new CreateEnterpriseRequest(
        "Empresa Teste",
        "12.345.678/0001-00",
        "teste@email.com",
        "11999999999", SubscriptionStatus.ACTIVE, PlanType.PRO);

    CreateEnterpriseUseCase useCase = new CreateEnterpriseUseCase(new CreateEnterpriseMapper(), repository);
    when(repository.save(any(Enterprise.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Enterprise resultado = useCase.execute(request);

    assertNotNull(resultado);
    assertEquals("Empresa Teste", resultado.getRazaoSocial());

    assertEquals(request.cnpj(), resultado.getCnpj());
    assertEquals(request.status(), resultado.getStatus());
    assertEquals(request.plano(), resultado.getPlano());
    assertNotNull(resultado.getCreatedAt());
    assertEquals(resultado.getCreatedAt(), resultado.getUpdatedAt());
    verify(repository).save(resultado);
  }

}
