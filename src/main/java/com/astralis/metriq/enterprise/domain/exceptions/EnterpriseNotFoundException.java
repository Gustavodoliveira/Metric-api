package com.astralis.metriq.enterprise.domain.exceptions;

public class EnterpriseNotFoundException extends RuntimeException {

  public EnterpriseNotFoundException() {
    super("Empresa não encontrada");
  }
}
