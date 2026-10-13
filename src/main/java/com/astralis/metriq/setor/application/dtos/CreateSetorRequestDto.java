package com.astralis.metriq.setor.application.dtos;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSetorRequestDto(@NotNull UUID enterpriseId, @NotBlank @Size(max = 100) String name,
    @Size(max = 255) String descricao, @NotNull Boolean ativo) {

}
