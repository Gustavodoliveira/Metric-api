package com.astralis.metriq.enterprise.application.mapper;

import java.sql.Date;
import org.springframework.stereotype.Component;
import com.astralis.metriq.enterprise.application.dto.CreateEnterpriseRequest;
import com.astralis.metriq.enterprise.domain.model.Enterprise;

@Component
public class CreateEnterpriseMapper {
  public Enterprise toDomain(CreateEnterpriseRequest request) {
    Date now = new Date(System.currentTimeMillis());
    return new Enterprise(null, request.razaoSocial(), request.cnpj(), request.email(),
        request.telefone(), request.status(), request.plano(), now, now);
  }
}
