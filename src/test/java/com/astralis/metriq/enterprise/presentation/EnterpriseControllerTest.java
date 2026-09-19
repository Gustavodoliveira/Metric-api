package com.astralis.metriq.enterprise.presentation;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.astralis.metriq.enterprise.application.usecase.*;
import com.astralis.metriq.enterprise.domain.model.Enterprise;

class EnterpriseControllerTest {
  private final CreateEnterpriseUseCase create = mock(CreateEnterpriseUseCase.class);
  private final FindEnterpriseByIdUseCase findById = mock(FindEnterpriseByIdUseCase.class);
  private final FindEnterpriseByCnpjUseCase findByCnpj = mock(FindEnterpriseByCnpjUseCase.class);
  private final DeleteEnterpriseByIdUseCase delete = mock(DeleteEnterpriseByIdUseCase.class);
  private MockMvc mvc;

  @BeforeEach
  void setUp() {
    mvc = MockMvcBuilders.standaloneSetup(new EnterpriseController(create, findByCnpj, findById, delete))
        .setControllerAdvice(new EnterpriseExceptionHandler()).build();
  }

  @Test
  void missingEnterpriseReturns404() throws Exception {
    UUID id = UUID.randomUUID();
    when(findById.executeFindEnterpriseById(id)).thenReturn(Optional.empty());
    when(findByCnpj.executeFindEnterpriseByCnpj("11222333000181")).thenReturn(Optional.empty());
    mvc.perform(get("/enterprise/by-id/{id}", id)).andExpect(status().isNotFound());
    mvc.perform(get("/enterprise/by-Cnpj/11222333000181")).andExpect(status().isNotFound());
  }

  @Test
  void responsePreservesJsonFieldNames() throws Exception {
    UUID id = UUID.randomUUID();
    Enterprise enterprise = new Enterprise();
    enterprise.setId(id);
    enterprise.setRazaoSocial("Empresa Teste");
    when(findById.executeFindEnterpriseById(id)).thenReturn(Optional.of(enterprise));
    mvc.perform(get("/enterprise/by-id/{id}", id)).andExpect(status().isOk())
        .andExpect(jsonPath("$.razao_social").value("Empresa Teste"))
        .andExpect(jsonPath("$.razaoSocial").doesNotExist());
  }

  @Test
  void invalidRequestDoesNotReachUseCase() throws Exception {
    mvc.perform(post("/enterprise/create").contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(status().isBadRequest());
    verifyNoInteractions(create);
  }

  @Test
  void validRequestAcceptsExistingJsonContract() throws Exception {
    when(create.execute(any())).thenReturn(new Enterprise());
    mvc.perform(post("/enterprise/create").contentType(MediaType.APPLICATION_JSON).content("""
        {"razao_social":"Empresa Teste","cnpj":"11.222.333/0001-81",
         "email":"teste@example.com","telefone":"11999999999","status":"ACTIVE","plano":"PRO"}
        """))
        .andExpect(status().isOk());
    verify(create).execute(argThat(request -> "Empresa Teste".equals(request.razaoSocial())));
  }

  @Test
  void invalidCnpjReturns400() throws Exception {
    when(findByCnpj.executeFindEnterpriseByCnpj("invalid"))
        .thenThrow(new IllegalArgumentException("CNPJ inválido"));
    mvc.perform(get("/enterprise/by-Cnpj/invalid")).andExpect(status().isBadRequest());
  }
}
