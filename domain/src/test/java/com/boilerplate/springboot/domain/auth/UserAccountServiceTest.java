package com.boilerplate.springboot.domain.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserAccountServiceTest {

  private InMemoryUsers users;
  private UserAccountService service;

  @BeforeEach
  void setUp() {
    users = new InMemoryUsers();
    InMemoryRoles roles = new InMemoryRoles();
    service = new UserAccountService(users, roles, raw -> "hash:" + raw);
  }

  @Test
  void createUserAssignsRoles() {
    UserAccount created =
        service.create("Admin@Example.com", "secret12", "Admin", Set.of(Role.ADMIN));
    assertThat(created.getEmail()).isEqualTo("admin@example.com");
    assertThat(created.getPasswordHash()).isEqualTo("hash:secret12");
    assertThat(created.getRoleNames()).containsExactly(Role.ADMIN);
  }

  @Test
  void duplicateEmailRejected() {
    service.create("a@example.com", "secret12", "A", Set.of(Role.USER));
    assertThatThrownBy(() -> service.create("a@example.com", "secret12", "B", Set.of(Role.USER)))
        .isInstanceOf(AuthException.class)
        .hasMessageContaining("already registered");
  }

  private static final class InMemoryUsers implements UserAccountRepository {
    private final AtomicLong seq = new AtomicLong(1);
    private final List<UserAccount> store = new ArrayList<>();

    @Override
    public UserAccount save(UserAccount user) {
      if (user.getId() == null) {
        UserAccount stored =
            new UserAccount(
                seq.getAndIncrement(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getDisplayName(),
                user.isEnabled(),
                Instant.now(),
                user.getRoleNames(),
                user.getPermissionCodes());
        store.add(stored);
        return stored;
      }
      store.removeIf(u -> u.getId().equals(user.getId()));
      store.add(user);
      return user;
    }

    @Override
    public Optional<UserAccount> findById(Long id) {
      return store.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<UserAccount> findByEmail(String email) {
      return store.stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    @Override
    public List<UserAccount> findAll() {
      return List.copyOf(store);
    }

    @Override
    public boolean existsByEmail(String email) {
      return findByEmail(email).isPresent();
    }

    @Override
    public long count() {
      return store.size();
    }
  }

  private static final class InMemoryRoles implements RoleRepository {
    @Override
    public List<Role> findAll() {
      return List.of(
          new Role(1L, Role.ADMIN, "Admin", Permission.Codes.all()),
          new Role(2L, Role.USER, "User", Set.of(Permission.Codes.ITEMS_READ)));
    }

    @Override
    public Optional<Role> findByName(String name) {
      return findAll().stream().filter(r -> r.getName().equals(name)).findFirst();
    }

    @Override
    public Optional<Role> findById(Long id) {
      return findAll().stream().filter(r -> r.getId().equals(id)).findFirst();
    }
  }
}
