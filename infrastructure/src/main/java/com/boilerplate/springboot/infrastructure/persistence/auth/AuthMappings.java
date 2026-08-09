package com.boilerplate.springboot.infrastructure.persistence.auth;

import com.boilerplate.springboot.domain.auth.Permission;
import com.boilerplate.springboot.domain.auth.Role;
import com.boilerplate.springboot.domain.auth.UserAccount;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

final class AuthMappings {

  private AuthMappings() {}

  static UserAccount toDomain(UserEntity entity) {
    Set<String> roleNames =
        entity.getRoles().stream()
            .map(RoleEntity::getName)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    Set<String> permissions =
        entity.getRoles().stream()
            .flatMap(role -> role.getPermissions().stream())
            .map(PermissionEntity::getCode)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    return new UserAccount(
        entity.getId(),
        entity.getEmail(),
        entity.getPasswordHash(),
        entity.getDisplayName(),
        entity.isEnabled(),
        entity.getCreatedAt(),
        roleNames,
        permissions);
  }

  static Role toDomain(RoleEntity entity) {
    Set<String> codes =
        entity.getPermissions().stream()
            .map(PermissionEntity::getCode)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    return new Role(entity.getId(), entity.getName(), entity.getDescription(), codes);
  }

  static Permission toDomain(PermissionEntity entity) {
    return new Permission(entity.getId(), entity.getCode(), entity.getDescription());
  }
}
