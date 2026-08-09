package com.boilerplate.springboot.infrastructure.persistence.auth;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPermissionRepository extends JpaRepository<PermissionEntity, Long> {}
