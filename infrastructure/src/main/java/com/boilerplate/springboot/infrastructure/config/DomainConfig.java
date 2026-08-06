package com.boilerplate.springboot.infrastructure.config;

import com.boilerplate.springboot.domain.item.ItemRepository;
import com.boilerplate.springboot.domain.item.ItemService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.boilerplate.springboot.infrastructure.persistence")
public class DomainConfig {

  @Bean
  ItemService itemService(ItemRepository itemRepository) {
    return new ItemService(itemRepository);
  }
}
