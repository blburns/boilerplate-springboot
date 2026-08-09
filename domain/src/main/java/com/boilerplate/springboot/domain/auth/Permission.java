package com.boilerplate.springboot.domain.auth;

import java.util.Objects;
import java.util.Set;

public final class Permission {

  private final Long id;
  private final String code;
  private final String description;

  public Permission(Long id, String code, String description) {
    this.id = id;
    this.code = Objects.requireNonNull(code, "code");
    this.description = description;
  }

  public Long getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }

  public static final class Codes {
    public static final String ADMIN_ACCESS = "admin:access";
    public static final String ITEMS_READ = "items:read";
    public static final String ITEMS_WRITE = "items:write";
    public static final String USERS_READ = "users:read";
    public static final String USERS_WRITE = "users:write";
    public static final String ROLES_READ = "roles:read";
    public static final String ROLES_WRITE = "roles:write";

    public static Set<String> all() {
      return Set.of(
          ADMIN_ACCESS, ITEMS_READ, ITEMS_WRITE, USERS_READ, USERS_WRITE, ROLES_READ, ROLES_WRITE);
    }

    private Codes() {}
  }
}
