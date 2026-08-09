package com.boilerplate.springboot.domain.auth;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class UserAccountService {

  private final UserAccountRepository userAccountRepository;
  private final RoleRepository roleRepository;
  private final Function<String, String> passwordHasher;

  public UserAccountService(
      UserAccountRepository userAccountRepository,
      RoleRepository roleRepository,
      Function<String, String> passwordHasher) {
    this.userAccountRepository = Objects.requireNonNull(userAccountRepository);
    this.roleRepository = Objects.requireNonNull(roleRepository);
    this.passwordHasher = Objects.requireNonNull(passwordHasher);
  }

  public List<UserAccount> findAll() {
    return userAccountRepository.findAll();
  }

  public UserAccount findById(Long id) {
    return userAccountRepository
        .findById(id)
        .orElseThrow(() -> new AuthException("User not found: " + id));
  }

  public UserAccount create(
      String email, String rawPassword, String displayName, Set<String> roleNames) {
    String normalized = normalizeEmail(email);
    if (userAccountRepository.existsByEmail(normalized)) {
      throw new AuthException("Email already registered: " + normalized);
    }
    Set<String> roles = validateRoles(roleNames);
    Instant now = Instant.now();
    UserAccount user =
        new UserAccount(
            null,
            normalized,
            passwordHasher.apply(rawPassword),
            displayName,
            true,
            now,
            roles,
            Set.of());
    return userAccountRepository.save(user);
  }

  public UserAccount update(Long id, String displayName, boolean enabled, Set<String> roleNames) {
    UserAccount existing = findById(id);
    return userAccountRepository.save(
        existing.withProfile(displayName, enabled, validateRoles(roleNames)));
  }

  public UserAccount resetPassword(Long id, String rawPassword) {
    UserAccount existing = findById(id);
    return userAccountRepository.save(existing.withPasswordHash(passwordHasher.apply(rawPassword)));
  }

  private Set<String> validateRoles(Set<String> roleNames) {
    if (roleNames == null || roleNames.isEmpty()) {
      throw new AuthException("At least one role is required");
    }
    Set<String> validated = new LinkedHashSet<>();
    for (String name : roleNames) {
      roleRepository.findByName(name).orElseThrow(() -> new AuthException("Unknown role: " + name));
      validated.add(name);
    }
    return validated;
  }

  private static String normalizeEmail(String email) {
    return Objects.requireNonNull(email, "email").trim().toLowerCase(Locale.ROOT);
  }
}
