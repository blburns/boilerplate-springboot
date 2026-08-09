package com.boilerplate.springboot.infrastructure.config;

import com.boilerplate.springboot.domain.auth.Permission;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/login", "/css/**", "/js/**", "/images/**", "/webjars/**", "/error")
                    .permitAll()
                    .requestMatchers(
                        "/actuator/health",
                        "/actuator/health/**",
                        "/actuator/info",
                        "/actuator/prometheus")
                    .permitAll()
                    .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/admin/users", "/admin/users/**")
                    .hasAuthority(Permission.Codes.USERS_WRITE)
                    .requestMatchers("/admin/users", "/admin/users/**")
                    .hasAuthority(Permission.Codes.USERS_READ)
                    .requestMatchers("/admin/roles", "/admin/roles/**")
                    .hasAuthority(Permission.Codes.ROLES_READ)
                    .requestMatchers("/admin", "/admin/**")
                    .hasAuthority(Permission.Codes.ADMIN_ACCESS)
                    .requestMatchers(HttpMethod.POST, "/items", "/items/**")
                    .hasAuthority(Permission.Codes.ITEMS_WRITE)
                    .requestMatchers(HttpMethod.GET, "/items", "/items/**")
                    .hasAuthority(Permission.Codes.ITEMS_READ)
                    .requestMatchers(HttpMethod.POST, "/api/v1/items", "/api/v1/items/**")
                    .hasAuthority(Permission.Codes.ITEMS_WRITE)
                    .requestMatchers(HttpMethod.PUT, "/api/v1/items/**")
                    .hasAuthority(Permission.Codes.ITEMS_WRITE)
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/items/**")
                    .hasAuthority(Permission.Codes.ITEMS_WRITE)
                    .requestMatchers(HttpMethod.GET, "/api/v1/items", "/api/v1/items/**")
                    .hasAuthority(Permission.Codes.ITEMS_READ)
                    .anyRequest()
                    .authenticated())
        .formLogin(
            form ->
                form.loginPage("/login")
                    .defaultSuccessUrl("/admin", true)
                    .failureUrl("/login?error")
                    .permitAll())
        .logout(
            logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
        .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
        .httpBasic(Customizer.withDefaults());
    return http.build();
  }
}
