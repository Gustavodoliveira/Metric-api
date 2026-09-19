package com.astralis.metriq.enterprise.application.dtos;

import com.astralis.metriq.enterprise.domain.enums.PlanType;
import com.astralis.metriq.enterprise.domain.enums.SubscriptionStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CNPJ;

public record CreateEnterpriseRequest(
    @JsonProperty("razao_social") @NotBlank String razaoSocial,
    @NotBlank @CNPJ String cnpj,
    @NotBlank @Email String email,
    @NotBlank String telefone,
    @NotNull SubscriptionStatus status,
    @NotNull PlanType plano) {
}
