package com.astralis.metriq.enterprise.domain.exceptions;

public class EnterpriseAlreadyExistsException extends RuntimeException {
  public EnterpriseAlreadyExistsException() {
    super("CNPJ já cadastrado");
  }
}
