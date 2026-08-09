package com.boilerplate.springboot.infrastructure.persistence.auth;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataRoleRepository extends JpaRepository<RoleEntity, Long> {

  Optional<RoleEntity> findByName(String name);
}
