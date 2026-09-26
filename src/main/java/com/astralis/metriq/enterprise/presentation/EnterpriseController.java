package com.astralis.metriq.enterprise.presentation;

import java.util.UUID;
import org.springframework.web.bind.annotation.PutMapping;
import com.astralis.metriq.enterprise.application.dtos.UpdateEnterpriseRequest;
import com.astralis.metriq.enterprise.application.useCases.UpdateEnterpriseUseCase;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.astralis.metriq.enterprise.application.dtos.CreateEnterpriseRequest;
import com.astralis.metriq.enterprise.application.useCases.CreateEnterpriseUseCase;
import com.astralis.metriq.enterprise.application.useCases.DeleteEnterpriseByIdUseCase;
import com.astralis.metriq.enterprise.application.useCases.FindEnterpriseByCnpjUseCase;
import com.astralis.metriq.enterprise.application.useCases.FindEnterpriseByIdUseCase;
import com.astralis.metriq.enterprise.domain.model.Enterprise;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import jakarta.validation.Valid;
import com.astralis.metriq.enterprise.presentation.dto.EnterpriseResponse;

@RestController
@RequestMapping("/enterprise")
@Tag(name = "Enterprises", description = "endpoints for company management")
@AllArgsConstructor
public class EnterpriseController {

  private final CreateEnterpriseUseCase createUseCase;

  private final FindEnterpriseByCnpjUseCase findEnterpriseByCnpjUseCase;

  private final FindEnterpriseByIdUseCase findEnterpriseByIdUseCase;

  private final DeleteEnterpriseByIdUseCase deleteEnterpriseByIdUseCase;

  private final UpdateEnterpriseUseCase updateEnterpriseUseCase;

  @Operation(summary = "create enterprise", description = "Create Enterprise and return its data")
  @PostMapping("/create")
  public ResponseEntity<EnterpriseResponse> createEnterprise(@Valid @RequestBody CreateEnterpriseRequest dto) {
    Enterprise enterprise = createUseCase.execute(dto);
    return ResponseEntity.ok(EnterpriseResponse.from(enterprise));
  }

  @GetMapping("/by-Cnpj/{cnpj}")
  public ResponseEntity<EnterpriseResponse> getEnterpriseByCnpj(@PathVariable("cnpj") String cnpj) {
    return ResponseEntity.of(findEnterpriseByCnpjUseCase.executeFindEnterpriseByCnpj(cnpj)
        .map(EnterpriseResponse::from));
  }

  @GetMapping("/by-id/{id}")
  public ResponseEntity<EnterpriseResponse> getEnterpriseById(@PathVariable("id") UUID id) {
    return ResponseEntity.of(findEnterpriseByIdUseCase.executeFindEnterpriseById(id)
        .map(EnterpriseResponse::from));
  }

  @Operation(summary = "update enterprise", description = "Update Enterprise and return its data")
  @PutMapping("/update-by-id/{id}")
  public ResponseEntity<EnterpriseResponse> updateEnterprise(@PathVariable("id") UUID id,
      @Valid @RequestBody UpdateEnterpriseRequest request) {
    return ResponseEntity.of(updateEnterpriseUseCase.execute(id, request).map(EnterpriseResponse::from));
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteEnterpriseById(@PathVariable("id") UUID id) {
    deleteEnterpriseByIdUseCase.executeDeleteEnterpriseById(id);
    return ResponseEntity.ok("Empresa Deletada com sucesso");
  }
}
