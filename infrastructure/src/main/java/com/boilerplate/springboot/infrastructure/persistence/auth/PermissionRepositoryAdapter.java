package com.boilerplate.springboot.infrastructure.persistence.auth;

import com.boilerplate.springboot.domain.auth.Permission;
import com.boilerplate.springboot.domain.auth.PermissionRepository;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class PermissionRepositoryAdapter implements PermissionRepository {

  private final SpringDataPermissionRepository permissions;

  public PermissionRepositoryAdapter(SpringDataPermissionRepository permissions) {
    this.permissions = permissions;
  }

  @Override
  public List<Permission> findAll() {
    return permissions.findAll().stream().map(AuthMappings::toDomain).toList();
  }
}
