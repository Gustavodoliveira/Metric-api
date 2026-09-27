package com.astralis.metriq.setor.domain.exceptions;

import java.util.UUID;

public class SetorNotFoundException extends RuntimeException {
  public SetorNotFoundException(UUID id) {
    super("Setor não encontrado");
  }

  public SetorNotFoundException(String message) {
    super(message);
  }
}
