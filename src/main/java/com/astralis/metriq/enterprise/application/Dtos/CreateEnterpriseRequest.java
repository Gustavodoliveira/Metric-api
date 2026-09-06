package com.astralis.metriq.enterprise.application.Dtos;

import com.astralis.metriq.enterprise.domain.enums.PlanType;
import com.astralis.metriq.enterprise.domain.enums.SubscriptionStatus;

public record CreateEnterpriseRequest(String razao_social, String cnpj, String email, String telefone,
    SubscriptionStatus status, PlanType plano) {

}
