package com.astralis.metriq.users.application.useCases;

import org.springframework.stereotype.Service;

import com.astralis.metriq.users.domain.exceptions.UserNotFoundException;
import com.astralis.metriq.users.domain.model.UserEntity;
import com.astralis.metriq.users.domain.repositories.UserRepository;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FindUserByEmailUseCase {
  private final UserRepository repository;

  public UserEntity execute(@Valid String email) {
    return repository.findByEmail(email).orElseThrow(() -> {
      throw new UserNotFoundException("Usuário não encontrado");
    });
  }
}
