package com.astralis.metriq.users.application.dtos;

import java.util.UUID;

import com.astralis.metriq.users.domain.enums.Status;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
    @NotNull UUID enterpriseId,
    @NotBlank String name,
    @NotBlank @Email String email,
    @NotBlank String senha,
    @NotBlank String perfil,
    @NotNull Status status) {

}
