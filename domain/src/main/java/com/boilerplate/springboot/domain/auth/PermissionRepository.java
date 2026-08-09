package com.boilerplate.springboot.domain.auth;

import java.util.List;

public interface PermissionRepository {

  List<Permission> findAll();
}
