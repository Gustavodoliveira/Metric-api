package com.astralis.metriq.setor.application.dtos;

import java.util.UUID;

public record CreateSetorRequestDto(UUID enterpriseId, String name, String descricao, Boolean ativo) {

}
