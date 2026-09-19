package com.astralis.metriq.enterprise.presentation.dto;

import java.sql.Date;
import java.util.UUID;
import com.astralis.metriq.enterprise.domain.enums.PlanType;
import com.astralis.metriq.enterprise.domain.enums.SubscriptionStatus;
import com.astralis.metriq.enterprise.domain.model.Enterprise;
import com.fasterxml.jackson.annotation.JsonProperty;

public record EnterpriseResponse(
    UUID id,
    @JsonProperty("razao_social") String razaoSocial,
    String cnpj, String email, String telefone,
    SubscriptionStatus status, PlanType plano,
    @JsonProperty("created_at") Date createdAt,
    @JsonProperty("updated_at") Date updatedAt) {
  public static EnterpriseResponse from(Enterprise enterprise) {
    return new EnterpriseResponse(enterprise.getId(), enterprise.getRazaoSocial(),
        enterprise.getCnpj(), enterprise.getEmail(), enterprise.getTelefone(),
        enterprise.getStatus(), enterprise.getPlano(), enterprise.getCreatedAt(), enterprise.getUpdatedAt());
  }
}
