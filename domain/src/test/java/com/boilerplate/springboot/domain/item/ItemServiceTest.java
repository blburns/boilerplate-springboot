package com.boilerplate.springboot.domain.item;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ItemServiceTest {

  private InMemoryItemRepository repository;
  private ItemService service;

  @BeforeEach
  void setUp() {
    repository = new InMemoryItemRepository();
    service = new ItemService(repository);
  }

  @Test
  void createAndFindById() {
    Item created = service.create("Notebook", "Lined");
    assertThat(created.getId()).isNotNull();
    assertThat(service.findById(created.getId()).getName()).isEqualTo("Notebook");
  }

  @Test
  void updateChangesName() {
    Item created = service.create("Old", null);
    Item updated = service.update(created.getId(), "New", "desc");
    assertThat(updated.getName()).isEqualTo("New");
    assertThat(updated.getDescription()).isEqualTo("desc");
  }

  @Test
  void deleteUnknownThrows() {
    assertThatThrownBy(() -> service.delete(99L)).isInstanceOf(ItemNotFoundException.class);
  }

  @Test
  void findUnknownThrows() {
    assertThatThrownBy(() -> service.findById(1L))
        .isInstanceOf(ItemNotFoundException.class)
        .hasMessageContaining("1");
  }

  private static final class InMemoryItemRepository implements ItemRepository {
    private final AtomicLong seq = new AtomicLong(1);
    private final List<Item> items = new ArrayList<>();

    @Override
    public Item save(Item item) {
      if (item.getId() == null) {
        Item stored =
            new Item(
                seq.getAndIncrement(),
                item.getName(),
                item.getDescription(),
                item.getCreatedAt(),
                item.getUpdatedAt());
        items.add(stored);
        return stored;
      }
      items.removeIf(i -> i.getId().equals(item.getId()));
      items.add(item);
      return item;
    }

    @Override
    public Optional<Item> findById(Long id) {
      return items.stream().filter(i -> i.getId().equals(id)).findFirst();
    }

    @Override
    public List<Item> findAll() {
      return List.copyOf(items);
    }

    @Override
    public void deleteById(Long id) {
      items.removeIf(i -> i.getId().equals(id));
    }

    @Override
    public boolean existsById(Long id) {
      return items.stream().anyMatch(i -> i.getId().equals(id));
    }
  }
}
