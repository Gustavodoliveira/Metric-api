package com.astralis.metriq.users.application.useCases;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.astralis.metriq.users.domain.exceptions.UserNotFoundException;
import com.astralis.metriq.users.domain.model.UserEntity;
import com.astralis.metriq.users.domain.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class FindUserByEmailUseCaseTest {

  @Mock
  private UserRepository repository;

  @InjectMocks
  private FindUserByEmailUseCase useCase;

  private final String identifier = "gustavo@astralis.com";

  @Test
  void execute_ShouldReturnUser_WhenUserExists() {
    UserEntity user = new UserEntity();
    when(repository.findByEmail(identifier)).thenReturn(Optional.of(user));

    assertSame(user, useCase.execute(identifier));

    verify(repository).findByEmail(identifier);
    verifyNoMoreInteractions(repository);
  }

  @Test
  void execute_ShouldThrowUserNotFound_WhenUserDoesNotExist() {
    when(repository.findByEmail(identifier)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> useCase.execute(identifier));

    verify(repository).findByEmail(identifier);
    verifyNoMoreInteractions(repository);
  }

  @Test
  void execute_ShouldPropagateRepositoryFailure() {
    RuntimeException failure = new RuntimeException("Falha na consulta");
    when(repository.findByEmail(identifier)).thenThrow(failure);

    assertSame(failure, assertThrows(RuntimeException.class, () -> useCase.execute(identifier)));
  }
}
