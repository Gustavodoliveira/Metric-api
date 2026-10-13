package com.astralis.metriq.setor.presentation;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.astralis.metriq.users.domain.model.UserEntity;
import com.astralis.metriq.users.domain.enums.Status;
import com.astralis.metriq.users.domain.repositories.UserRepository;
import com.astralis.metriq.setor.application.useCases.FindSetorByEnterpriseIdUseCase;

@SpringBootTest
class SetorSecurityTest {
  @Autowired private WebApplicationContext context;
  @Autowired private PasswordEncoder encoder;
  @MockitoBean private UserRepository users;
  @MockitoBean private FindSetorByEnterpriseIdUseCase find;
  private final UUID company = UUID.randomUUID();
  private MockMvc mvc;

  @BeforeEach
  void setUp() {
    mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    UserEntity user = new UserEntity();
    user.setEmail("user@example.com");
    user.setSenha(encoder.encode("test-password"));
    user.setEmpresa_id(company);
    user.setStatus(Status.ACTIVE);
    when(users.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    when(find.execute(company)).thenReturn(List.of());
  }

  @Test
  void anonymousRequestReturns401() throws Exception {
    mvc.perform(get("/setor/get/{id}", company)).andExpect(status().isUnauthorized());
    verifyNoInteractions(find);
  }

  @Test
  void wrongPasswordReturns401() throws Exception {
    mvc.perform(get("/setor/get/{id}", company).with(httpBasic("user@example.com", "wrong")))
        .andExpect(status().isUnauthorized());
    verifyNoInteractions(find);
  }

  @Test
  void correctCredentialsAllowOwnCompany() throws Exception {
    mvc.perform(get("/setor/get/{id}", company).with(httpBasic("user@example.com", "test-password")))
        .andExpect(status().isOk());
    verify(find).execute(company);
  }

  @Test
  void correctCredentialsDenyOtherCompany() throws Exception {
    mvc.perform(get("/setor/get/{id}", UUID.randomUUID())
        .with(httpBasic("user@example.com", "test-password"))).andExpect(status().isForbidden());
    verifyNoInteractions(find);
  }

  @Test
  void inactiveUserCannotAuthenticate() throws Exception {
    users.findByEmail("user@example.com").orElseThrow().setStatus(Status.INACTIVE);
    mvc.perform(get("/setor/get/{id}", company).with(httpBasic("user@example.com", "test-password")))
        .andExpect(status().isUnauthorized());
    verifyNoInteractions(find);
  }
}
