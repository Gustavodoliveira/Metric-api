package com.astralis.metriq.users.presentation;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.astralis.metriq.users.application.dtos.CreateUserRequest;
import com.astralis.metriq.users.application.useCases.CreateUserUseCase;
import com.astralis.metriq.users.application.useCases.DeleteUserByIdUseCase;
import com.astralis.metriq.users.application.useCases.FindUserByEmailUseCase;
import com.astralis.metriq.users.application.useCases.FindUserByIdUseCase;
import com.astralis.metriq.users.domain.model.UserEntity;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Tag(name = "User", description = "endpoints for user management")
public class UserController {
  private final CreateUserUseCase createUserUseCase;

  private final DeleteUserByIdUseCase deleteUserByIdUseCase;

  private final FindUserByEmailUseCase findUserByEmailUseCase;

  private final FindUserByIdUseCase findUserByIdUseCase;

  @PostMapping("/create")
  public ResponseEntity<UserEntity> postUser(@Valid @RequestBody CreateUserRequest request) {
    UserEntity userEntity = createUserUseCase.execute(request);
    return ResponseEntity.ok(userEntity);
  }

  @GetMapping("/getBy-email/{email}")
  public ResponseEntity<UserEntity> getUserByEmail(@PathVariable("email") String email) {
    UserEntity user = findUserByEmailUseCase.execute(email);
    return ResponseEntity.ok(user);
  }

  @GetMapping("/getBy-id/{id}")
  public ResponseEntity<UserEntity> getUserById(@PathVariable("id") UUID id) {
    UserEntity user = findUserByIdUseCase.execute(id);
    return ResponseEntity.ok(user);
  }

  @DeleteMapping("/delete-by-id/{id}/{idEnterprise}")
  public ResponseEntity<String> deleteUserById(@PathVariable("id") UUID id,
      @PathVariable("idEnterprise") UUID enterpriseId) {
    deleteUserByIdUseCase.execute(id, enterpriseId);
    return ResponseEntity.ok("Deletado com sucesso");
  }

}
