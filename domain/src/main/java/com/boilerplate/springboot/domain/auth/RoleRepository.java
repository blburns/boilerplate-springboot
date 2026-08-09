package com.boilerplate.springboot.domain.auth;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {

  List<Role> findAll();

  Optional<Role> findByName(String name);

  Optional<Role> findById(Long id);
}
