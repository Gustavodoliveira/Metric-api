package com.astralis.metriq.setor.presentation;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.astralis.metriq.security.EnterpriseAccess;
import com.astralis.metriq.setor.application.dtos.SetorResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.astralis.metriq.setor.application.dtos.CreateSetorRequestDto;
import com.astralis.metriq.setor.application.useCases.CreateSetorUseCase;
import com.astralis.metriq.setor.application.useCases.DeleteSetorByIdUseCase;
import com.astralis.metriq.setor.application.useCases.FindSetorByEnterpriseIdUseCase;
import com.astralis.metriq.setor.application.useCases.FindSetorByNameAndEnterpriseIdUseCase;
import com.astralis.metriq.setor.domain.model.SetorEntity;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/setor")
@AllArgsConstructor
public class SetorController {

  private final CreateSetorUseCase createSetorUseCase;

  private final FindSetorByEnterpriseIdUseCase findSetorByEnterpriseIdUseCase;

  private final EnterpriseAccess enterpriseAccess;

  private final FindSetorByNameAndEnterpriseIdUseCase findSetorByNameAndEnterpriseIdUseCase;

  private final DeleteSetorByIdUseCase deleteSetorByIdUseCase;

  @PostMapping("/create")
  public ResponseEntity<SetorResponse> createSetor(@Valid @RequestBody CreateSetorRequestDto requestDto, Authentication authentication) {
    enterpriseAccess.check(authentication, requestDto.enterpriseId());
    SetorEntity entity = createSetorUseCase.execute(requestDto);
    return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/setor/get/{name}/{enterpriseId}")
        .buildAndExpand(entity.getName(), entity.getEnterpriseId()).encode().toUri())
        .body(SetorResponse.from(entity));
  }

  @GetMapping("/get/{name}/{id}")
  public ResponseEntity<SetorResponse> getEntityByNameAndEnterpriseID(@PathVariable("name") String name,
      @PathVariable("id") UUID id, Authentication authentication) {
    enterpriseAccess.check(authentication, id);
    SetorEntity entity = findSetorByNameAndEnterpriseIdUseCase.execute(name, id);
    return ResponseEntity.ok(SetorResponse.from(entity));
  }

  @GetMapping("/get/{enterpriseId}")
  public ResponseEntity<List<SetorResponse>> getListSetorByEnterpriseId(@PathVariable("enterpriseId") UUID enterpriseId, Authentication authentication) {
    enterpriseAccess.check(authentication, enterpriseId);
    List<SetorEntity> entities = findSetorByEnterpriseIdUseCase.execute(enterpriseId);
    return ResponseEntity.ok(entities.stream().map(SetorResponse::from).toList());
  }

  @DeleteMapping("/delete/{id}/{enterpriseId}")
  public ResponseEntity<String> deleteSetorById(@PathVariable("id") UUID id,
      @PathVariable("enterpriseId") UUID enterpriseId, Authentication authentication) {
    enterpriseAccess.check(authentication, enterpriseId);
    deleteSetorByIdUseCase.execute(id, enterpriseId);
    return ResponseEntity.ok("Deletado com sucesso");
  }
}
