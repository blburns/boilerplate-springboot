package com.boilerplate.springboot.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataItemRepository extends JpaRepository<ItemEntity, Long> {}
