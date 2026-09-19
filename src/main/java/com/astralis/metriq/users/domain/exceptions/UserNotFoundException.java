package com.astralis.metriq.users.domain.exceptions;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(UUID id) {
    super("Usuário não encontrado com o ID: " + id);
  }

  public UserNotFoundException(String message) {
    super(message);
  }
}
