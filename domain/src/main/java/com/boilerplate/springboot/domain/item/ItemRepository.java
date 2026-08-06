package com.boilerplate.springboot.domain.item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

  Item save(Item item);

  Optional<Item> findById(Long id);

  List<Item> findAll();

  void deleteById(Long id);

  boolean existsById(Long id);
}
