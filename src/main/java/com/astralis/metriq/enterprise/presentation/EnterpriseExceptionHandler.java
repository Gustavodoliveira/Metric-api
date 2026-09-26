package com.astralis.metriq.enterprise.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = EnterpriseController.class)
public class EnterpriseExceptionHandler {
  @ExceptionHandler(com.astralis.metriq.enterprise.domain.exceptions.EnterpriseAlreadyExistsException.class)
  public ProblemDetail handleDuplicate(com.astralis.metriq.enterprise.domain.exceptions.EnterpriseAlreadyExistsException exception) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ProblemDetail handleInvalidArgument(IllegalArgumentException exception) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
  }
}
