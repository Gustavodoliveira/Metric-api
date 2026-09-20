package com.astralis.metriq.users.application.useCases;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.astralis.metriq.users.domain.exceptions.UserNotFoundException;
import com.astralis.metriq.users.domain.model.UserEntity;
import com.astralis.metriq.users.domain.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FindUserByIdUseCase {

  private final UserRepository repository;

  public UserEntity execute(UUID id) {
    return repository.findById(id).orElseThrow(() -> {
      throw new UserNotFoundException("Usuário não encontrado");
    });
  }
}
