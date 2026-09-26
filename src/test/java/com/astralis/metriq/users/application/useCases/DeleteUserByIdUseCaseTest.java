package com.astralis.metriq.users.application.useCases;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.astralis.metriq.users.domain.exceptions.UserNotFoundException;
import com.astralis.metriq.users.domain.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class DeleteUserByIdUseCaseTest {

  @Mock
  private UserRepository repository;

  @InjectMocks
  private DeleteUserByIdUseCase useCase;

  private final UUID userId = UUID.randomUUID();
  private final UUID enterpriseId = UUID.randomUUID();

  @Test
  void execute_ShouldDeleteUser_UsingUserAndEnterpriseIds() {
    useCase.execute(userId, enterpriseId);

    verify(repository).deleteUser(userId, enterpriseId);
    verifyNoMoreInteractions(repository);
  }

  @Test
  void execute_ShouldPropagateUserNotFound() {
    UserNotFoundException failure = new UserNotFoundException(userId);
    doThrow(failure).when(repository).deleteUser(userId, enterpriseId);

    assertSame(failure, assertThrows(UserNotFoundException.class,
        () -> useCase.execute(userId, enterpriseId)));

    verify(repository).deleteUser(userId, enterpriseId);
    verifyNoMoreInteractions(repository);
  }

  @Test
  void execute_ShouldPropagateRepositoryFailure() {
    RuntimeException failure = new RuntimeException("Falha na exclusão");
    doThrow(failure).when(repository).deleteUser(userId, enterpriseId);

    assertSame(failure, assertThrows(RuntimeException.class,
        () -> useCase.execute(userId, enterpriseId)));
  }
}
