package com.boilerplate.springboot.domain.item;

public class ItemNotFoundException extends RuntimeException {

  private final Long itemId;

  public ItemNotFoundException(Long itemId) {
    super("Item not found: " + itemId);
    this.itemId = itemId;
  }

  public Long getItemId() {
    return itemId;
  }
}
