package com.boilerplate.springboot.domain.item;

import java.util.List;
import java.util.Objects;

public class ItemService {

  private final ItemRepository itemRepository;

  public ItemService(ItemRepository itemRepository) {
    this.itemRepository = Objects.requireNonNull(itemRepository);
  }

  public List<Item> findAll() {
    return itemRepository.findAll();
  }

  public Item findById(Long id) {
    return itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
  }

  public Item create(String name, String description) {
    return itemRepository.save(Item.create(name, description));
  }

  public Item update(Long id, String name, String description) {
    Item existing = findById(id);
    return itemRepository.save(existing.update(name, description));
  }

  public void delete(Long id) {
    if (!itemRepository.existsById(id)) {
      throw new ItemNotFoundException(id);
    }
    itemRepository.deleteById(id);
  }
}
