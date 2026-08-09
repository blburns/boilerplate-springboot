package com.boilerplate.springboot.infrastructure.config;

import com.boilerplate.springboot.domain.auth.RoleRepository;
import com.boilerplate.springboot.domain.auth.UserAccountRepository;
import com.boilerplate.springboot.domain.auth.UserAccountService;
import com.boilerplate.springboot.domain.item.ItemRepository;
import com.boilerplate.springboot.domain.item.ItemService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableJpaRepositories(basePackages = "com.boilerplate.springboot.infrastructure.persistence")
public class DomainConfig {

  @Bean
  ItemService itemService(ItemRepository itemRepository) {
    return new ItemService(itemRepository);
  }

  @Bean
  UserAccountService userAccountService(
      UserAccountRepository userAccountRepository,
      RoleRepository roleRepository,
      PasswordEncoder passwordEncoder) {
    return new UserAccountService(userAccountRepository, roleRepository, passwordEncoder::encode);
  }
}
