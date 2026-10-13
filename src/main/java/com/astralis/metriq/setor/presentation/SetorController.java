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

import com.astralis.metriq.enterprise.application.useCases.FindEnterpriseByIdUseCase;
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

  private final FindEnterpriseByIdUseCase findEnterpriseByIdUseCase;

  private final FindSetorByNameAndEnterpriseIdUseCase findSetorByNameAndEnterpriseIdUseCase;

  private final DeleteSetorByIdUseCase deleteSetorByIdUseCase;

  @PostMapping("/create")
  public ResponseEntity<?> createSetor(@RequestBody CreateSetorRequestDto requestDto) {
    SetorEntity entity = createSetorUseCase.execute(requestDto);
    return ResponseEntity.ok(entity);
  }

  @GetMapping("/get/{name}/{id}")
  public ResponseEntity<SetorEntity> getEntityByNameAndEnterpriseID(@PathVariable("name") String name,
      @PathVariable("id") UUID id) {
    SetorEntity entity = findSetorByNameAndEnterpriseIdUseCase.execute(name, id);
    return ResponseEntity.ok(entity);
  }

  @GetMapping("/get/{enterpriseId}")
  public ResponseEntity<List<SetorEntity>> getListSetorByEnterpriseId(@PathVariable("enterpriseId") UUID enterpriseId) {
    List<SetorEntity> entities = findSetorByEnterpriseIdUseCase.execute(enterpriseId);
    return ResponseEntity.status(200).body(entities);
  }

  @DeleteMapping("/delete/{id}/{enterpriseId}")
  public ResponseEntity<String> deleteSetorById(@PathVariable("id") UUID id,
      @PathVariable("enterpriseId") UUID enterpriseId) {
    deleteSetorByIdUseCase.execute(id, enterpriseId);
    return ResponseEntity.ok("Deletado com sucesso");
  }
}
