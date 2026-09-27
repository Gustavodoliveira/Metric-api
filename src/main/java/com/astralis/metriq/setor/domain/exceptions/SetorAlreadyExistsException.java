package com.astralis.metriq.setor.domain.exceptions;

public class SetorAlreadyExistsException extends RuntimeException {
  public SetorAlreadyExistsException() {
    super("Já existe um setor com esse nome na empresa");
  }
}
