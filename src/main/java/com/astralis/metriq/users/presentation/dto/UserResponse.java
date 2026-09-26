package com.astralis.metriq.users.presentation.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import com.astralis.metriq.users.domain.enums.Status;
import com.astralis.metriq.users.domain.model.UserEntity;

public record UserResponse(UUID id, UUID enterpriseId, String name, String email,
    String perfil, Status status, LocalDateTime createdAt, LocalDateTime updatedAt) {
  public static UserResponse from(UserEntity user) {
    return new UserResponse(user.getId(), user.getEmpresa_id(), user.getName(), user.getEmail(),
        user.getPerfil(), user.getStatus(), user.getCreatedAt(), user.getUpdateAt());
  }
}
