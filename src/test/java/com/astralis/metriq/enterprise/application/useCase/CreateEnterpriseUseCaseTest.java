package com.astralis.metriq.enterprise.application.useCase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.astralis.metriq.enterprise.application.Dtos.CreateEnterpriseRequest;
import com.astralis.metriq.enterprise.domain.enums.PlanType;
import com.astralis.metriq.enterprise.domain.enums.SubscriptionStatus;
import com.astralis.metriq.enterprise.domain.model.Enterprise;
import com.astralis.metriq.enterprise.domain.repositories.EnterpriseRepository;
import com.astralis.metriq.enterprise.infrastructure.persistence.EnterpriseMapper;

@ExtendWith(MockitoExtension.class)
public class CreateEnterpriseUseCaseTest {

  @Mock
  private EnterpriseRepository repository;

  @Mock
  private EnterpriseMapper mapper;

  @InjectMocks
  private CreateEnterpriseUseCase useCase;

  @Test
  void shouldCreateEnterpriseSuccessfully() {
    CreateEnterpriseRequest request = new CreateEnterpriseRequest(
        "Empresa Teste",
        "12.345.678/0001-00",
        "teste@email.com",
        "11999999999", SubscriptionStatus.ACTIVE, PlanType.PRO);

    Enterprise enterprise = new Enterprise();
    enterprise.setRazao_social("Empresa Teste");

    when(mapper.toDomain(request))
        .thenReturn(enterprise);

    when(repository.save(enterprise))
        .thenReturn(enterprise);

    Enterprise resultado = useCase.execute(request);

    assertNotNull(resultado);
    assertEquals("Empresa Teste", resultado.getRazao_social());

    verify(mapper).toDomain(request);
    verify(repository).save(enterprise);
  }

}
