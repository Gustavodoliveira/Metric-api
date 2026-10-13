package com.astralis.metriq.security;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import com.astralis.metriq.users.domain.enums.Status;
import com.astralis.metriq.users.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EnterpriseAccess {
  private final UserRepository users;

  public void check(Authentication authentication, UUID enterpriseId) {
    if (authentication == null || !authentication.isAuthenticated()
        || authentication instanceof AnonymousAuthenticationToken) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    var user = users.findByEmail(authentication.getName())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));
    if (user.getStatus() != Status.ACTIVE || enterpriseId == null
        || !enterpriseId.equals(user.getEmpresa_id())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
  }
}
