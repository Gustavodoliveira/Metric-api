package com.astralis.metriq.enterprise.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = EnterpriseController.class)
public class EnterpriseExceptionHandler {
  @ExceptionHandler(IllegalArgumentException.class)
  public ProblemDetail handleInvalidArgument(IllegalArgumentException exception) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
  }
}
