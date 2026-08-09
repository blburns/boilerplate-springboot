package com.boilerplate.springboot.infrastructure.persistence.auth;

import com.boilerplate.springboot.domain.auth.AuthException;
import com.boilerplate.springboot.domain.auth.UserAccount;
import com.boilerplate.springboot.domain.auth.UserAccountRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class UserAccountRepositoryAdapter implements UserAccountRepository {

  private final SpringDataUserRepository users;
  private final SpringDataRoleRepository roles;

  public UserAccountRepositoryAdapter(
      SpringDataUserRepository users, SpringDataRoleRepository roles) {
    this.users = users;
    this.roles = roles;
  }

  @Override
  public UserAccount save(UserAccount user) {
    UserEntity entity;
    if (user.getId() == null) {
      entity = new UserEntity(user.getEmail(), user.getPasswordHash(), user.getDisplayName());
      entity.setEnabled(user.isEnabled());
    } else {
      entity =
          users
              .findById(user.getId())
              .orElseThrow(() -> new AuthException("User not found: " + user.getId()));
      entity.setDisplayName(user.getDisplayName());
      entity.setEnabled(user.isEnabled());
      entity.setPasswordHash(user.getPasswordHash());
    }
    entity.setRoles(resolveRoles(user.getRoleNames()));
    return AuthMappings.toDomain(users.save(entity));
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<UserAccount> findById(Long id) {
    return users.findById(id).map(AuthMappings::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<UserAccount> findByEmail(String email) {
    return users.findByEmailIgnoreCase(email).map(AuthMappings::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserAccount> findAll() {
    return users.findAll().stream().map(AuthMappings::toDomain).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsByEmail(String email) {
    return users.existsByEmailIgnoreCase(email);
  }

  @Override
  @Transactional(readOnly = true)
  public long count() {
    return users.count();
  }

  private Set<RoleEntity> resolveRoles(Set<String> roleNames) {
    Set<RoleEntity> resolved = new HashSet<>();
    for (String name : roleNames) {
      RoleEntity role =
          roles.findByName(name).orElseThrow(() -> new AuthException("Unknown role: " + name));
      resolved.add(role);
    }
    return resolved;
  }
}
