package com.boilerplate.springboot.infrastructure.security;

import com.boilerplate.springboot.domain.auth.UserAccount;
import com.boilerplate.springboot.domain.auth.UserAccountRepository;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

  private final UserAccountRepository userAccountRepository;

  public DatabaseUserDetailsService(UserAccountRepository userAccountRepository) {
    this.userAccountRepository = userAccountRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserAccount account =
        userAccountRepository
            .findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    return User.withUsername(account.getEmail())
        .password(account.getPasswordHash())
        .disabled(!account.isEnabled())
        .authorities(authorities(account))
        .build();
  }

  private static Collection<? extends GrantedAuthority> authorities(UserAccount account) {
    Set<GrantedAuthority> authorities = new LinkedHashSet<>();
    for (String role : account.getRoleNames()) {
      authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
    }
    authorities.addAll(
        account.getPermissionCodes().stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet()));
    return authorities;
  }
}
