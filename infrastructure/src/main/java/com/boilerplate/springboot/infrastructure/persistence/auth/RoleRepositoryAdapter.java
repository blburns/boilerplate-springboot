package com.boilerplate.springboot.infrastructure.persistence.auth;

import com.boilerplate.springboot.domain.auth.Role;
import com.boilerplate.springboot.domain.auth.RoleRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class RoleRepositoryAdapter implements RoleRepository {

  private final SpringDataRoleRepository roles;

  public RoleRepositoryAdapter(SpringDataRoleRepository roles) {
    this.roles = roles;
  }

  @Override
  public List<Role> findAll() {
    return roles.findAll().stream().map(AuthMappings::toDomain).toList();
  }

  @Override
  public Optional<Role> findByName(String name) {
    return roles.findByName(name).map(AuthMappings::toDomain);
  }

  @Override
  public Optional<Role> findById(Long id) {
    return roles.findById(id).map(AuthMappings::toDomain);
  }
}
