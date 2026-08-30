package com.astralis.metriq.enterprise.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.astralis.metriq.enterprise.application.Dtos.CreateEnterpriseRequest;
import com.astralis.metriq.enterprise.application.useCase.CreateEnterpriseUseCase;
import com.astralis.metriq.enterprise.domain.model.Enterprise;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/enterprise")
@AllArgsConstructor
public class EnterpriseController {

  private final CreateEnterpriseUseCase CreateUseCase;

  @PostMapping("/create")
  public ResponseEntity<Enterprise> createEnterprise(@RequestBody CreateEnterpriseRequest dto) {
    Enterprise enterprise = CreateUseCase.execute(dto);
    return ResponseEntity.ok(enterprise);
  }
}
