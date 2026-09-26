package com.astralis.metriq.users.presentation;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.astralis.metriq.users.application.useCases.*;
import com.astralis.metriq.users.domain.enums.Status;
import com.astralis.metriq.users.domain.exceptions.*;
import com.astralis.metriq.users.domain.model.UserEntity;

class UserControllerTest {
  private final CreateUserUseCase create = mock(CreateUserUseCase.class);
  private final FindUserByEmailUseCase findByEmail = mock(FindUserByEmailUseCase.class);
  private final FindUserByIdUseCase findById = mock(FindUserByIdUseCase.class);
  private final DeleteUserByIdUseCase deleteUser = mock(DeleteUserByIdUseCase.class);
  private final UpdateUserUseCase update = mock(UpdateUserUseCase.class);
  private final UUID id = UUID.randomUUID();
  private final UUID enterpriseId = UUID.randomUUID();
  private MockMvc mvc;
  private UserEntity user;

  private String body(String email) {
    return """
        {"enterpriseId":"%s","name":"Gustavo","email":%s,
         "senha":"senha123","perfil":"ADMIN","status":"ACTIVE"}
        """.formatted(enterpriseId, email == null ? "null" : "\"" + email + "\"");
  }

  @BeforeEach
  void setup() {
    mvc = MockMvcBuilders.standaloneSetup(new UserController(create, deleteUser, findByEmail, findById, update))
        .setControllerAdvice(new UserExceptionHandler()).build();
    user = new UserEntity();
    user.setId(id);
    user.setEmpresa_id(enterpriseId);
    user.setName("Gustavo");
    user.setEmail("gustavo@astralis.com");
    user.setSenha("secret-password-hash");
    user.setPerfil("ADMIN");
    user.setStatus(Status.ACTIVE);
  }

  @Test
  void createReturnsPublicUserData() throws Exception {
    when(create.execute(any())).thenReturn(user);
    mvc.perform(post("/user/create").contentType(MediaType.APPLICATION_JSON).content(body(user.getEmail())))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.enterpriseId").value(enterpriseId.toString()))
        .andExpect(jsonPath("$.name").value("Gustavo"))
        .andExpect(jsonPath("$.email").value(user.getEmail()))
        .andExpect(jsonPath("$.perfil").value("ADMIN"))
        .andExpect(jsonPath("$.status").value("ACTIVE"))
        .andExpect(jsonPath("$.senha").doesNotExist());
    verify(create).execute(argThat(r -> r.enterpriseId().equals(enterpriseId)
        && r.email().equals(user.getEmail()) && r.senha().equals("senha123")));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   ", "invalid-email"})
  void invalidEmailDoesNotReachCreateOrUpdate(String email) throws Exception {
    mvc.perform(post("/user/create").contentType(MediaType.APPLICATION_JSON).content(body(email)))
        .andExpect(status().isBadRequest());
    mvc.perform(put("/user/update-by-id/{id}/{enterpriseId}", id, enterpriseId)
        .contentType(MediaType.APPLICATION_JSON).content(body(email)))
        .andExpect(status().isBadRequest());
    verifyNoInteractions(create, update);
  }

  @Test
  void missingRequiredFieldsAreRejected() throws Exception {
    mvc.perform(post("/user/create").contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(status().isBadRequest());
    mvc.perform(put("/user/update-by-id/{id}/{enterpriseId}", id, enterpriseId)
        .contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(status().isBadRequest());
    verifyNoInteractions(create, update);
  }

  @Test
  void duplicateEmailReturns409() throws Exception {
    when(create.execute(any())).thenThrow(new UserAlreadyExistsException(user.getEmail()));
    when(update.execute(eq(id), eq(enterpriseId), any())).thenThrow(new UserAlreadyExistsException(user.getEmail()));
    mvc.perform(post("/user/create").contentType(MediaType.APPLICATION_JSON).content(body(user.getEmail())))
        .andExpect(status().isConflict()).andExpect(jsonPath("$.status").value(409));
    mvc.perform(put("/user/update-by-id/{id}/{enterpriseId}", id, enterpriseId)
        .contentType(MediaType.APPLICATION_JSON).content(body(user.getEmail())))
        .andExpect(status().isConflict());
  }

  @Test
  void lookupRoutesReturnUserWithoutPassword() throws Exception {
    when(findById.execute(id)).thenReturn(user);
    when(findByEmail.execute(user.getEmail())).thenReturn(user);
    mvc.perform(get("/user/getBy-id/{id}", id)).andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id.toString())).andExpect(jsonPath("$.senha").doesNotExist());
    mvc.perform(get("/user/getBy-email/{email}", user.getEmail())).andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value(user.getEmail())).andExpect(jsonPath("$.senha").doesNotExist());
    verify(findById).execute(id);
    verify(findByEmail).execute(user.getEmail());
  }

  @Test
  void missingUserReturns404ForAllExistingUserOperations() throws Exception {
    when(findById.execute(id)).thenThrow(new UserNotFoundException(id));
    when(findByEmail.execute(user.getEmail())).thenThrow(new UserNotFoundException(id));
    doThrow(new UserNotFoundException(id)).when(deleteUser).execute(id, enterpriseId);
    when(update.execute(eq(id), eq(enterpriseId), any())).thenThrow(new UserNotFoundException(id));
    mvc.perform(get("/user/getBy-id/{id}", id)).andExpect(status().isNotFound());
    mvc.perform(get("/user/getBy-email/{email}", user.getEmail())).andExpect(status().isNotFound());
    mvc.perform(delete("/user/delete-by-id/{id}/{enterpriseId}", id, enterpriseId)).andExpect(status().isNotFound());
    mvc.perform(put("/user/update-by-id/{id}/{enterpriseId}", id, enterpriseId)
        .contentType(MediaType.APPLICATION_JSON).content(body(user.getEmail()))).andExpect(status().isNotFound());
  }

  @Test
  void deletePassesBothIdsToUseCase() throws Exception {
    mvc.perform(delete("/user/delete-by-id/{id}/{enterpriseId}", id, enterpriseId)).andExpect(status().isOk());
    verify(deleteUser).execute(id, enterpriseId);
  }

  @Test
  void updateReturnsUserWithoutPassword() throws Exception {
    when(update.execute(eq(id), eq(enterpriseId), any())).thenReturn(user);
    mvc.perform(put("/user/update-by-id/{id}/{enterpriseId}", id, enterpriseId)
        .contentType(MediaType.APPLICATION_JSON).content(body(user.getEmail())))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.senha").doesNotExist());
    verify(update).execute(eq(id), eq(enterpriseId), argThat(r -> r.name().equals("Gustavo")
        && r.email().equals(user.getEmail()) && r.senha().equals("senha123")));
  }

  @Test
  void invalidUuidReturns400() throws Exception {
    mvc.perform(get("/user/getBy-id/invalid")).andExpect(status().isBadRequest());
    verifyNoInteractions(findById);
  }
}
