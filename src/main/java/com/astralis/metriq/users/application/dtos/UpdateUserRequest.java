package com.astralis.metriq.users.application.dtos;

import com.astralis.metriq.users.domain.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRequest(@NotBlank String name, @NotBlank @Email String email,
    @NotBlank String senha, @NotBlank String perfil, @NotNull Status status) {
}
