package com.astralis.metriq.enterprise.presentation;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.astralis.metriq.enterprise.application.Dtos.CreateEnterpriseRequest;
import com.astralis.metriq.enterprise.application.useCase.CreateEnterpriseUseCase;
import com.astralis.metriq.enterprise.application.useCase.DeleteEnterpriseByIdUseCase;
import com.astralis.metriq.enterprise.application.useCase.FindEnterpriseByCnpjUseCase;
import com.astralis.metriq.enterprise.application.useCase.FindEnterpriseByIdUseCase;
import com.astralis.metriq.enterprise.domain.model.Enterprise;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/enterprise")
@Tag(name = "Enterprises", description = "endpoints for company management")
@AllArgsConstructor
public class EnterpriseController {

  private final CreateEnterpriseUseCase CreateUseCase;

  private final FindEnterpriseByCnpjUseCase findEnterpriseByCnpjUseCase;

  private final FindEnterpriseByIdUseCase findEnterpriseByIdUseCase;

  private final DeleteEnterpriseByIdUseCase deleteEnterpriseByIdUseCase;

  @Operation(summary = "create enterprise", description = "Create Enterprise and return its data")
  @PostMapping("/create")
  public ResponseEntity<Enterprise> createEnterprise(@RequestBody CreateEnterpriseRequest dto) {
    Enterprise enterprise = CreateUseCase.execute(dto);
    return ResponseEntity.ok(enterprise);
  }

  @GetMapping("/by-Cnpj/{cnpj}")
  public ResponseEntity<Optional<Enterprise>> getEnterpriseByCnpj(@PathVariable("cnpj") String cnpj) {
    Optional<Enterprise> enterprise = findEnterpriseByCnpjUseCase.executeFindEnterpriseByCnpj(cnpj);
    return ResponseEntity.ok(enterprise);
  }

  @GetMapping("/by-id/{id}")
  public ResponseEntity<Optional<Enterprise>> getEnterpriseById(@PathVariable("id") UUID id) {
    Optional<Enterprise> enterprise = findEnterpriseByIdUseCase.executeFindEnterpriseById(id);
    return ResponseEntity.ok(enterprise);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteEnterpriseById(@PathVariable("id") UUID id) {
    String response = deleteEnterpriseByIdUseCase.executeDeleteEnterpriseById(id);
    return ResponseEntity.ok(response);
  }
}
