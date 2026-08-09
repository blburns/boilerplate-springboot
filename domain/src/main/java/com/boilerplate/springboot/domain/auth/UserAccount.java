package com.boilerplate.springboot.domain.auth;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public final class UserAccount {

  private final Long id;
  private final String email;
  private final String passwordHash;
  private final String displayName;
  private final boolean enabled;
  private final Instant createdAt;
  private final Set<String> roleNames;
  private final Set<String> permissionCodes;

  public UserAccount(
      Long id,
      String email,
      String passwordHash,
      String displayName,
      boolean enabled,
      Instant createdAt,
      Set<String> roleNames,
      Set<String> permissionCodes) {
    this.id = id;
    this.email = Objects.requireNonNull(email, "email");
    this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash");
    this.displayName = displayName;
    this.enabled = enabled;
    this.createdAt = createdAt;
    this.roleNames =
        Collections.unmodifiableSet(new LinkedHashSet<>(roleNames == null ? Set.of() : roleNames));
    this.permissionCodes =
        Collections.unmodifiableSet(
            new LinkedHashSet<>(permissionCodes == null ? Set.of() : permissionCodes));
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public String getDisplayName() {
    return displayName;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Set<String> getRoleNames() {
    return roleNames;
  }

  public Set<String> getPermissionCodes() {
    return permissionCodes;
  }

  public boolean hasPermission(String code) {
    return permissionCodes.contains(code);
  }

  public UserAccount withPasswordHash(String newHash) {
    return new UserAccount(
        id, email, newHash, displayName, enabled, createdAt, roleNames, permissionCodes);
  }

  public UserAccount withProfile(String displayName, boolean enabled, Set<String> roleNames) {
    return new UserAccount(
        id, email, passwordHash, displayName, enabled, createdAt, roleNames, permissionCodes);
  }
}
