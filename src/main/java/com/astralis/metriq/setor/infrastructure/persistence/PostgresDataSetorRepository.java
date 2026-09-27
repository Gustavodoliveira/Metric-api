package com.astralis.metriq.setor.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostgresDataSetorRepository extends JpaRepository<SetorJpaEntity, UUID> {

  Optional<SetorJpaEntity> findByIdAndEnterprise_Id(UUID id, UUID enterpriseId);

  @Query("select setor from SetorJpaEntity setor where setor.enterprise.id = :enterpriseId")
  List<SetorJpaEntity> findByEnterpriseId(@Param("enterpriseId") UUID enterpriseId);

  @Query("select setor from SetorJpaEntity setor where setor.nome = :nome and setor.enterprise.id = :enterpriseId")
  Optional<SetorJpaEntity> findByNomeAndEnterpriseId(@Param("nome") String nome,
      @Param("enterpriseId") UUID enterpriseId);
}
