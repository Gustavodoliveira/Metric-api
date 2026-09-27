package com.astralis.metriq.setor.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostgresDataSetorRepository extends JpaRepository<SetorJpaEntity, UUID> {

  @Query("select setor from SetorJpaEntity setor where setor.enterprise_id.id = :enterpriseId")
  Optional<SetorJpaEntity> findByEnterpriseId(@Param("enterpriseId") UUID enterpriseId);

  @Query("select setor from SetorJpaEntity setor where setor.nome = :nome and setor.enterprise_id.id = :enterpriseId")
  Optional<SetorJpaEntity> findByNomeAndEnterpriseId(@Param("nome") String nome,
      @Param("enterpriseId") UUID enterpriseId);
}
