package com.astralis.metriq.users.application.useCases;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.astralis.metriq.users.application.dtos.UpdateUserRequest;
import com.astralis.metriq.users.domain.exceptions.UserAlreadyExistsException;
import com.astralis.metriq.users.domain.exceptions.UserNotFoundException;
import com.astralis.metriq.users.domain.model.UserEntity;
import com.astralis.metriq.users.domain.repositories.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UpdateUserUseCase {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public UserEntity execute(UUID id, UUID enterpriseId, UpdateUserRequest request) {
    UserEntity user = repository.findById(id)
        .filter(existing -> enterpriseId.equals(existing.getEmpresa_id()))
        .orElseThrow(() -> new UserNotFoundException(id));
    repository.findByEmail(request.email()).filter(existing -> !id.equals(existing.getId()))
        .ifPresent(existing -> { throw new UserAlreadyExistsException(request.email()); });
    String password = passwordEncoder.encode(request.senha());
    user.setName(request.name());
    user.setEmail(request.email());
    user.setSenha(password);
    user.setPerfil(request.perfil());
    user.setStatus(request.status());
    user.setUpdateAt(LocalDateTime.now());
    return repository.updateUser(user);
  }
}
