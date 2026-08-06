package com.boilerplate.springboot.api.item;

import com.boilerplate.springboot.domain.item.Item;
import java.time.Instant;

public record ItemResponse(
    Long id, String name, String description, Instant createdAt, Instant updatedAt) {

  public static ItemResponse from(Item item) {
    return new ItemResponse(
        item.getId(),
        item.getName(),
        item.getDescription(),
        item.getCreatedAt(),
        item.getUpdatedAt());
  }
}
