package com.astralis.metriq.users.presentation;

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
import org.springframework.web.bind.annotation.PutMapping;
import com.astralis.metriq.users.application.dtos.UpdateUserRequest;
import com.astralis.metriq.users.application.useCases.UpdateUserUseCase;
import com.astralis.metriq.users.presentation.dto.UserResponse;
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

  private final UpdateUserUseCase updateUserUseCase;

  @PostMapping("/create")
  public ResponseEntity<UserResponse> postUser(@Valid @RequestBody CreateUserRequest request) {
    UserEntity userEntity = createUserUseCase.execute(request);
    return ResponseEntity.ok(UserResponse.from(userEntity));
  }

  @GetMapping("/getBy-email/{email}")
  public ResponseEntity<UserResponse> getUserByEmail(@PathVariable("email") String email) {
    UserEntity user = findUserByEmailUseCase.execute(email);
    return ResponseEntity.ok(UserResponse.from(user));
  }

  @GetMapping("/getBy-id/{id}")
  public ResponseEntity<UserResponse> getUserById(@PathVariable("id") UUID id) {
    UserEntity user = findUserByIdUseCase.execute(id);
    return ResponseEntity.ok(UserResponse.from(user));
  }

  @PutMapping("/update-by-id/{id}/{idEnterprise}")
  public ResponseEntity<UserResponse> updateUser(@PathVariable("id") UUID id,
      @PathVariable("idEnterprise") UUID enterpriseId, @Valid @RequestBody UpdateUserRequest request) {
    return ResponseEntity.ok(UserResponse.from(updateUserUseCase.execute(id, enterpriseId, request)));
  }

  @DeleteMapping("/delete-by-id/{id}/{idEnterprise}")
  public ResponseEntity<String> deleteUserById(@PathVariable("id") UUID id,
      @PathVariable("idEnterprise") UUID enterpriseId) {
    deleteUserByIdUseCase.execute(id, enterpriseId);
    return ResponseEntity.ok("Deletado com sucesso");
  }

}
