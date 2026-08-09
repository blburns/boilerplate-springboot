package com.boilerplate.springboot.domain.auth;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public final class Role {

  private final Long id;
  private final String name;
  private final String description;
  private final Set<String> permissionCodes;

  public Role(Long id, String name, String description, Set<String> permissionCodes) {
    this.id = id;
    this.name = Objects.requireNonNull(name, "name");
    this.description = description;
    this.permissionCodes =
        Collections.unmodifiableSet(
            new LinkedHashSet<>(permissionCodes == null ? Set.of() : permissionCodes));
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Set<String> getPermissionCodes() {
    return permissionCodes;
  }

  public static final String ADMIN = "ADMIN";
  public static final String USER = "USER";
}
