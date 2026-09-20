package com.astralis.metriq.users.application.useCases;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.astralis.metriq.users.domain.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DeleteUserByIdUseCase {
  private final UserRepository repository;

  public void execute(UUID id, UUID enterpriseId) {
    repository.deleteUser(id, enterpriseId);
    return;
  }
}
