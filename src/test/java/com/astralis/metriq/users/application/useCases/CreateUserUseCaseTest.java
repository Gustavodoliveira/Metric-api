package com.astralis.metriq.users.application.useCases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.astralis.metriq.users.application.dtos.CreateUserRequest;
import com.astralis.metriq.users.application.mapper.CreateUserMapper;
import com.astralis.metriq.users.domain.enums.Status;
import com.astralis.metriq.users.domain.exceptions.UserAlreadyExistsException;
import com.astralis.metriq.users.domain.model.UserEntity;
import com.astralis.metriq.users.domain.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

  @Mock
  private UserRepository repository;

  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  private CreateUserUseCase useCase;
  private CreateUserRequest request;

  @BeforeEach
  void setup() {
    useCase = new CreateUserUseCase(new CreateUserMapper(), repository, passwordEncoder);
    request = new CreateUserRequest(UUID.randomUUID(), "Gustavo", "gustavo@astralis.com",
        "senha123", "ADMIN", Status.ACTIVE);
  }

  @Test
  void execute_ShouldCreateUser_WhenRequestIsValid() {
    UserEntity createdUser = new UserEntity();
    createdUser.setId(UUID.randomUUID());
    when(repository.findByEmail(request.email())).thenReturn(Optional.empty());
    when(repository.createUser(any(UserEntity.class))).thenReturn(createdUser);

    UserEntity result = useCase.execute(request);

    assertSame(createdUser, result);
    ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
    verify(repository).findByEmail(request.email());
    verify(repository).createUser(captor.capture());
    UserEntity mappedUser = captor.getValue();
    assertAll(
        () -> assertNull(mappedUser.getId()),
        () -> assertEquals(request.enterpriseId(), mappedUser.getEmpresa_id()),
        () -> assertEquals(request.name(), mappedUser.getName()),
        () -> assertEquals(request.email(), mappedUser.getEmail()),
        () -> assertNotEquals(request.senha(), mappedUser.getSenha()),
        () -> assertTrue(passwordEncoder.matches(request.senha(), mappedUser.getSenha())),
        () -> assertEquals(request.perfil(), mappedUser.getPerfil()),
        () -> assertEquals(request.status(), mappedUser.getStatus()),
        () -> assertNotNull(mappedUser.getCreatedAt()),
        () -> assertEquals(mappedUser.getCreatedAt(), mappedUser.getUpdateAt()));
    verifyNoMoreInteractions(repository);
  }

  @Test
  void execute_ShouldRejectDuplicateEmail_WithoutCreatingUser() {
    when(repository.findByEmail(request.email())).thenReturn(Optional.of(new UserEntity()));

    assertThrows(UserAlreadyExistsException.class, () -> useCase.execute(request));

    verify(repository).findByEmail(request.email());
    verify(repository, never()).createUser(any());
    verifyNoMoreInteractions(repository);
  }

  @Test
  void execute_ShouldPropagateLookupFailure_WithoutCreatingUser() {
    RuntimeException failure = new RuntimeException("Falha na consulta");
    when(repository.findByEmail(request.email())).thenThrow(failure);

    assertSame(failure, assertThrows(RuntimeException.class, () -> useCase.execute(request)));

    verify(repository, never()).createUser(any());
  }

  @Test
  void execute_ShouldPropagateCreationFailure() {
    RuntimeException failure = new RuntimeException("Falha na persistência");
    when(repository.findByEmail(request.email())).thenReturn(Optional.empty());
    when(repository.createUser(any(UserEntity.class))).thenThrow(failure);

    assertSame(failure, assertThrows(RuntimeException.class, () -> useCase.execute(request)));
  }
}
