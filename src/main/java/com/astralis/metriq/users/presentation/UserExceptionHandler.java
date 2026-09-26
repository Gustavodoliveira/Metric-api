package com.astralis.metriq.users.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.astralis.metriq.users.domain.exceptions.UserNotFoundException;
import com.astralis.metriq.users.domain.exceptions.UserAlreadyExistsException;

@RestControllerAdvice(assignableTypes = UserController.class)
public class UserExceptionHandler {
  @ExceptionHandler(UserNotFoundException.class)
  public ProblemDetail handleNotFound(UserNotFoundException exception) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ProblemDetail handleDuplicate(UserAlreadyExistsException exception) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "E-mail já cadastrado");
  }
}
