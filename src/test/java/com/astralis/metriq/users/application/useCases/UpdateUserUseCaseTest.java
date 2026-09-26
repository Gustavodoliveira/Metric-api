package com.astralis.metriq.users.application.useCases;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.astralis.metriq.users.application.dtos.UpdateUserRequest;
import com.astralis.metriq.users.domain.enums.Status;
import com.astralis.metriq.users.domain.exceptions.*;
import com.astralis.metriq.users.domain.model.UserEntity;
import com.astralis.metriq.users.domain.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseTest {
  @Mock
  private UserRepository repository;
  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
  private final UUID id = UUID.randomUUID();
  private final UUID enterpriseId = UUID.randomUUID();
  private final UpdateUserRequest request = new UpdateUserRequest("Novo nome", "novo@example.com",
      "nova-senha", "USER", Status.INACTIVE);
  private UserEntity user;
  private UpdateUserUseCase useCase;

  @BeforeEach
  void setup() {
    useCase = new UpdateUserUseCase(repository, encoder);
    user = new UserEntity(id, enterpriseId, "Antigo", "antigo@example.com", "old-hash", "ADMIN",
        Status.ACTIVE, LocalDateTime.of(2026, 1, 1, 0, 0), LocalDateTime.of(2026, 1, 1, 0, 0));
  }

  @Test
  void updatesFieldsAndHashesPasswordWhilePreservingIdentityAndCreationDate() {
    LocalDateTime createdAt = user.getCreatedAt();
    when(repository.findById(id)).thenReturn(Optional.of(user));
    when(repository.findByEmail(request.email())).thenReturn(Optional.empty());
    UserEntity saved = new UserEntity();
    when(repository.updateUser(user)).thenReturn(saved);

    assertSame(saved, useCase.execute(id, enterpriseId, request));

    verify(repository).updateUser(user);
    assertAll(
        () -> assertEquals(id, user.getId()),
        () -> assertEquals(enterpriseId, user.getEmpresa_id()),
        () -> assertEquals(createdAt, user.getCreatedAt()),
        () -> assertTrue(user.getUpdateAt().isAfter(createdAt)),
        () -> assertEquals(request.name(), user.getName()),
        () -> assertEquals(request.email(), user.getEmail()),
        () -> assertEquals(request.perfil(), user.getPerfil()),
        () -> assertEquals(request.status(), user.getStatus()),
        () -> assertNotEquals(request.senha(), user.getSenha()),
        () -> assertTrue(encoder.matches(request.senha(), user.getSenha())));
  }

  @Test
  void allowsKeepingOwnEmail() {
    user.setEmail(request.email());
    when(repository.findById(id)).thenReturn(Optional.of(user));
    when(repository.findByEmail(request.email())).thenReturn(Optional.of(user));
    useCase.execute(id, enterpriseId, request);
    verify(repository).updateUser(user);
  }

  @Test
  void rejectsEmailOwnedByAnotherUserWithoutChangingUser() {
    UserEntity other = new UserEntity();
    other.setId(UUID.randomUUID());
    when(repository.findById(id)).thenReturn(Optional.of(user));
    when(repository.findByEmail(request.email())).thenReturn(Optional.of(other));
    assertThrows(UserAlreadyExistsException.class, () -> useCase.execute(id, enterpriseId, request));
    verify(repository, never()).updateUser(any());
    assertEquals("Antigo", user.getName());
    assertEquals("old-hash", user.getSenha());
  }

  @Test
  void rejectsMissingUser() {
    when(repository.findById(id)).thenReturn(Optional.empty());
    assertThrows(UserNotFoundException.class, () -> useCase.execute(id, enterpriseId, request));
    verify(repository).findById(id);
    verifyNoMoreInteractions(repository);
  }

  @Test
  void rejectsUserFromAnotherEnterprise() {
    when(repository.findById(id)).thenReturn(Optional.of(user));
    assertThrows(UserNotFoundException.class, () -> useCase.execute(id, UUID.randomUUID(), request));
    verify(repository).findById(id);
    verifyNoMoreInteractions(repository);
  }

  @Test
  void propagatesPersistenceFailure() {
    when(repository.findById(id)).thenReturn(Optional.of(user));
    when(repository.findByEmail(request.email())).thenReturn(Optional.empty());
    RuntimeException failure = new RuntimeException("Falha na atualização");
    when(repository.updateUser(user)).thenThrow(failure);
    assertSame(failure, assertThrows(RuntimeException.class, () -> useCase.execute(id, enterpriseId, request)));
  }
}
