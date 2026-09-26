package com.astralis.metriq.users.application.useCases;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.astralis.metriq.users.application.dtos.CreateUserRequest;
import com.astralis.metriq.users.application.mapper.CreateUserMapper;
import com.astralis.metriq.users.domain.exceptions.UserAlreadyExistsException;
import com.astralis.metriq.users.domain.model.UserEntity;
import com.astralis.metriq.users.domain.repositories.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CreateUserUseCase {

  private final CreateUserMapper mapper;

  private final UserRepository repository;

  private final PasswordEncoder passwordEncoder;

  public UserEntity execute(CreateUserRequest user) {
    UserEntity entity = mapper.toDomain(user);
    repository.findByEmail(user.email()).ifPresent(
        ex -> {
          throw new UserAlreadyExistsException(user.email());
        });
    entity.setSenha(passwordEncoder.encode(user.senha()));
    return repository.createUser(entity);
  }
}
