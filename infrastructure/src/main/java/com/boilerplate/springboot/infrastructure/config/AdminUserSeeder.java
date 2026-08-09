package com.boilerplate.springboot.infrastructure.config;

import com.boilerplate.springboot.domain.auth.Role;
import com.boilerplate.springboot.domain.auth.UserAccountRepository;
import com.boilerplate.springboot.domain.auth.UserAccountService;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Ensures a bootstrap admin exists for local/demo environments.
 *
 * <p>Default credentials: {@code admin@example.com} / {@code admin123}
 */
@Component
public class AdminUserSeeder implements ApplicationRunner {

  private static final Logger log = LoggerFactory.getLogger(AdminUserSeeder.class);

  public static final String DEFAULT_ADMIN_EMAIL = "admin@example.com";
  public static final String DEFAULT_ADMIN_PASSWORD = "admin123";

  private final UserAccountRepository userAccountRepository;
  private final UserAccountService userAccountService;

  public AdminUserSeeder(
      UserAccountRepository userAccountRepository, UserAccountService userAccountService) {
    this.userAccountRepository = userAccountRepository;
    this.userAccountService = userAccountService;
  }

  @Override
  public void run(ApplicationArguments args) {
    if (userAccountRepository.count() > 0) {
      return;
    }
    userAccountService.create(
        DEFAULT_ADMIN_EMAIL, DEFAULT_ADMIN_PASSWORD, "Administrator", Set.of(Role.ADMIN));
    log.warn(
        "Seeded default admin user {} / {} — change this password before production use",
        DEFAULT_ADMIN_EMAIL,
        DEFAULT_ADMIN_PASSWORD);
  }
}
