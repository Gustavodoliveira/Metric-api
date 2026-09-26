package com.astralis.metriq.enterprise.application.useCases;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.astralis.metriq.enterprise.application.dtos.UpdateEnterpriseRequest;
import com.astralis.metriq.enterprise.domain.exceptions.EnterpriseAlreadyExistsException;
import com.astralis.metriq.enterprise.domain.model.Enterprise;
import com.astralis.metriq.enterprise.domain.repositories.EnterpriseRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UpdateEnterpriseUseCase {
  private final EnterpriseRepository repository;

  @Transactional
  public Optional<Enterprise> execute(UUID id, UpdateEnterpriseRequest request) {
    return repository.findById(id).map(enterprise -> {
      repository.findByCnpj(request.cnpj()).filter(existing -> !id.equals(existing.getId()))
          .ifPresent(existing -> { throw new EnterpriseAlreadyExistsException(); });
      enterprise.setRazaoSocial(request.razaoSocial());
      enterprise.setCnpj(request.cnpj());
      enterprise.setEmail(request.email());
      enterprise.setTelefone(request.telefone());
      enterprise.setStatus(request.status());
      enterprise.setPlano(request.plano());
      enterprise.setUpdatedAt(LocalDateTime.now());
      return repository.save(enterprise);
    });
  }
}
