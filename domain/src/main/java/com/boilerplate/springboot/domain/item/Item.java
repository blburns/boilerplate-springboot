package com.boilerplate.springboot.domain.item;

import java.time.Instant;
import java.util.Objects;

public final class Item {

  private final Long id;
  private final String name;
  private final String description;
  private final Instant createdAt;
  private final Instant updatedAt;

  public Item(Long id, String name, String description, Instant createdAt, Instant updatedAt) {
    this.id = id;
    this.name = Objects.requireNonNull(name, "name");
    this.description = description;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public static Item create(String name, String description) {
    Instant now = Instant.now();
    return new Item(null, name, description, now, now);
  }

  public Item withId(Long id) {
    return new Item(id, name, description, createdAt, updatedAt);
  }

  public Item update(String name, String description) {
    return new Item(id, name, description, createdAt, Instant.now());
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }
}
