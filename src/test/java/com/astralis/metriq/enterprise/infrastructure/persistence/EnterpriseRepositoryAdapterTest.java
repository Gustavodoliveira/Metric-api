package com.astralis.metriq.enterprise.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class EnterpriseRepositoryAdapterTest {
  @Test
  void existenceRequiresBothCnpjAndId() {
    var repository = mock(PostgresDataEnterpriseRepository.class);
    var adapter = new EnterpriseRepositoryAdapter(repository, new EnterpriseMapper());
    UUID id = UUID.randomUUID();
    String cnpj = "11.222.333/0001-81";
    when(repository.existsByCnpjAndId(cnpj, id)).thenReturn(true);
    assertTrue(adapter.existsByCnpjAndEnterpriseId(cnpj, id));
    assertFalse(adapter.existsByCnpjAndEnterpriseId(cnpj, UUID.randomUUID()));
    assertFalse(adapter.existsByCnpjAndEnterpriseId("missing", id));
  }
}
