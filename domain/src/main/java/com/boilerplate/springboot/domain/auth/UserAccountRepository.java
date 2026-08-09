package com.boilerplate.springboot.domain.auth;

import java.util.List;
import java.util.Optional;

public interface UserAccountRepository {

  UserAccount save(UserAccount user);

  Optional<UserAccount> findById(Long id);

  Optional<UserAccount> findByEmail(String email);

  List<UserAccount> findAll();

  boolean existsByEmail(String email);

  long count();
}
