package com.boilerplate.springboot.infrastructure.persistence;

import com.boilerplate.springboot.domain.item.Item;
import com.boilerplate.springboot.domain.item.ItemRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ItemRepositoryAdapter implements ItemRepository {

  private final SpringDataItemRepository jpaRepository;

  public ItemRepositoryAdapter(SpringDataItemRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Item save(Item item) {
    ItemEntity entity;
    if (item.getId() == null) {
      entity = new ItemEntity(item.getName(), item.getDescription());
    } else {
      entity =
          jpaRepository
              .findById(item.getId())
              .orElseGet(() -> new ItemEntity(item.getName(), item.getDescription()));
      entity.setName(item.getName());
      entity.setDescription(item.getDescription());
    }
    return toDomain(jpaRepository.save(entity));
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Item> findById(Long id) {
    return jpaRepository.findById(id).map(this::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Item> findAll() {
    return jpaRepository.findAll().stream().map(this::toDomain).toList();
  }

  @Override
  public void deleteById(Long id) {
    jpaRepository.deleteById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsById(Long id) {
    return jpaRepository.existsById(id);
  }

  private Item toDomain(ItemEntity entity) {
    return new Item(
        entity.getId(),
        entity.getName(),
        entity.getDescription(),
        entity.getCreatedAt(),
        entity.getUpdatedAt());
  }
}
