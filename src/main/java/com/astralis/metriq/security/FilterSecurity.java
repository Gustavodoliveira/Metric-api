package com.astralis.metriq.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.Customizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.astralis.metriq.users.domain.repositories.UserRepository;
import com.astralis.metriq.users.domain.enums.Status;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class FilterSecurity {
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .httpBasic(Customizer.withDefaults())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/setor/**").authenticated()
            .anyRequest().permitAll())
        .build();
  }

  @Bean
  public UserDetailsService userDetailsService(UserRepository users) {
    return email -> {
      var user = users.findByEmail(email)
          .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
      return User.withUsername(user.getEmail()).password(user.getSenha())
          .authorities("USER").disabled(user.getStatus() != Status.ACTIVE).build();
    };
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
    return authConfiguration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
