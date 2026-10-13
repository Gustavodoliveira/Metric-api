package com.astralis.metriq.setor.presentation;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.astralis.metriq.security.EnterpriseAccess;
import com.astralis.metriq.users.domain.repositories.UserRepository;
import com.astralis.metriq.users.domain.model.UserEntity;
import com.astralis.metriq.users.domain.enums.Status;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.junit.jupiter.params.provider.CsvSource;
import java.util.Optional;
import com.astralis.metriq.setor.application.dtos.CreateSetorRequestDto;
import com.astralis.metriq.setor.application.useCases.*;
import com.astralis.metriq.setor.domain.model.SetorEntity;

class SetorControllerTest {
  private final CreateSetorUseCase create = mock(CreateSetorUseCase.class);
  private final FindSetorByEnterpriseIdUseCase findByEnterprise = mock(FindSetorByEnterpriseIdUseCase.class);
  private final UserRepository users = mock(UserRepository.class);
  private final FindSetorByNameAndEnterpriseIdUseCase findByName = mock(FindSetorByNameAndEnterpriseIdUseCase.class);
  private final DeleteSetorByIdUseCase delete = mock(DeleteSetorByIdUseCase.class);
  private final UUID id = UUID.fromString("11111111-1111-1111-1111-111111111111");
  private final UUID enterpriseId = UUID.fromString("22222222-2222-2222-2222-222222222222");
  private MockMvc mvc;

  @BeforeEach
  void setUp() {
    UserEntity user = new UserEntity();
    user.setEmail("user@example.com");
    user.setEmpresa_id(enterpriseId);
    user.setStatus(Status.ACTIVE);
    when(users.findByEmail("user@example.com")).thenReturn(Optional.of(user));
    mvc = MockMvcBuilders.standaloneSetup(
        new SetorController(create, findByEnterprise, new EnterpriseAccess(users), findByName, delete))
        .defaultRequest(get("/").principal(
            UsernamePasswordAuthenticationToken.authenticated("user@example.com", null, List.of())))
        .build();
  }

  @Test
  void createReadsJsonAndReturnsCreatedSetor() throws Exception {
    CreateSetorRequestDto request = new CreateSetorRequestDto(enterpriseId, "Financeiro", "Contas", true);
    when(create.execute(request)).thenReturn(setor());

    mvc.perform(post("/setor/create").contentType(MediaType.APPLICATION_JSON).content("""
        {"enterpriseId":"%s","name":"Financeiro","descricao":"Contas","ativo":true}
        """.formatted(enterpriseId)))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "http://localhost/setor/get/Financeiro/" + enterpriseId))
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.enterpriseId").value(enterpriseId.toString()))
        .andExpect(jsonPath("$.name").value("Financeiro"))
        .andExpect(jsonPath("$.descricao").value("Contas"))
        .andExpect(jsonPath("$.ativo").value(true));
    verify(create).execute(request);
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "{", "{}", "{\"enterpriseId\":\"invalid\"}"})
  void invalidCreateBodyReturns400WithoutCallingUseCase(String body) throws Exception {
    mvc.perform(post("/setor/create").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isBadRequest());
    verifyNoInteractions(create);
  }

  @Test
  void findByNameBindsNameAndEnterpriseId() throws Exception {
    when(findByName.execute("Recursos Humanos", enterpriseId)).thenReturn(setor());
    mvc.perform(get("/setor/get/{name}/{id}", "Recursos Humanos", enterpriseId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.name").value("Financeiro"));
    verify(findByName).execute("Recursos Humanos", enterpriseId);
    verifyNoInteractions(findByEnterprise);
  }

  @Test
  void findByEnterpriseReturnsList() throws Exception {
    when(findByEnterprise.execute(enterpriseId)).thenReturn(List.of(setor()));
    mvc.perform(get("/setor/get/{enterpriseId}", enterpriseId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].id").value(id.toString()))
        .andExpect(jsonPath("$[0].enterpriseId").value(enterpriseId.toString()));
    verify(findByEnterprise).execute(enterpriseId);
    verifyNoInteractions(findByName);
  }

  @Test
  void findByEnterpriseReturnsEmptyList() throws Exception {
    when(findByEnterprise.execute(enterpriseId)).thenReturn(List.of());
    mvc.perform(get("/setor/get/{enterpriseId}", enterpriseId))
        .andExpect(status().isOk()).andExpect(content().json("[]"));
    verify(findByEnterprise).execute(enterpriseId);
  }

  @Test
  void deleteBindsBothIdsAndReturnsSuccessMessage() throws Exception {
    mvc.perform(delete("/setor/delete/{id}/{enterpriseId}", id, enterpriseId))
        .andExpect(status().isOk()).andExpect(content().string("Deletado com sucesso"));
    verify(delete).execute(id, enterpriseId);
  }

  @ParameterizedTest
  @ValueSource(strings = {"/setor/get/invalid", "/setor/get/Financeiro/invalid"})
  void invalidGetUuidReturns400WithoutCallingUseCases(String path) throws Exception {
    mvc.perform(get(path)).andExpect(status().isBadRequest());
    verifyNoInteractions(findByName, findByEnterprise);
  }

  @ParameterizedTest
  @ValueSource(strings = {"id", "enterpriseId"})
  void invalidDeleteUuidReturns400WithoutCallingUseCase(String invalidField) throws Exception {
    mvc.perform(delete("/setor/delete/{id}/{enterpriseId}",
        invalidField.equals("id") ? "invalid" : id.toString(),
        invalidField.equals("enterpriseId") ? "invalid" : enterpriseId.toString()))
        .andExpect(status().isBadRequest());
    verifyNoInteractions(delete);
  }

  @ParameterizedTest
  @ValueSource(strings = {"name", "descricao", "enterpriseId", "ativo"})
  void invalidFieldsReturn400(String field) throws Exception {
    String name = field.equals("name") ? " " : "Financeiro";
    String description = field.equals("descricao") ? "x".repeat(256) : "Contas";
    String company = field.equals("enterpriseId") ? "null" : "\"" + enterpriseId + "\"";
    String active = field.equals("ativo") ? "null" : "true";
    mvc.perform(post("/setor/create").contentType(MediaType.APPLICATION_JSON).content(
        "{\"enterpriseId\":%s,\"name\":\"%s\",\"descricao\":\"%s\",\"ativo\":%s}"
            .formatted(company, name, description, active)))
        .andExpect(status().isBadRequest());
    verifyNoInteractions(create);
  }

  @ParameterizedTest
  @CsvSource({"create,401", "list,401", "name,401", "delete,401",
      "create,403", "list,403", "name,403", "delete,403"})
  void unauthorizedRequestsNeverReachUseCases(String route, int expectedStatus) throws Exception {
    var request = switch (route) {
      case "create" -> post("/setor/create").contentType(MediaType.APPLICATION_JSON).content(
          "{\"enterpriseId\":\"%s\",\"name\":\"Financeiro\",\"ativo\":true}".formatted(enterpriseId));
      case "list" -> get("/setor/get/{enterpriseId}", enterpriseId);
      case "name" -> get("/setor/get/{name}/{id}", "Financeiro", enterpriseId);
      default -> delete("/setor/delete/{id}/{enterpriseId}", id, enterpriseId);
    };
    request.principal(UsernamePasswordAuthenticationToken.authenticated("unknown@example.com", null, List.of()));
    when(users.findByEmail("unknown@example.com")).thenReturn(Optional.empty());
    if (expectedStatus == 401) {
      request.principal(UsernamePasswordAuthenticationToken.unauthenticated("user@example.com", null));
    } else {
      UserEntity otherUser = new UserEntity();
      otherUser.setStatus(Status.ACTIVE);
      otherUser.setEmpresa_id(UUID.randomUUID());
      when(users.findByEmail("unknown@example.com")).thenReturn(Optional.of(otherUser));
    }
    mvc.perform(request).andExpect(status().is(expectedStatus));
    verifyNoInteractions(create, findByName, findByEnterprise, delete);
  }

  private SetorEntity setor() {
    return new SetorEntity(id, enterpriseId, "Financeiro", "Contas", true, null, null);
  }
}
