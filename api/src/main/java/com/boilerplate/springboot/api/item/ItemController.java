package com.boilerplate.springboot.api.item;

import com.boilerplate.springboot.domain.item.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/items")
@Tag(name = "Items", description = "Sample CRUD resource for the boilerplate")
public class ItemController {

  private final ItemService itemService;

  public ItemController(ItemService itemService) {
    this.itemService = itemService;
  }

  @GetMapping
  @Operation(summary = "List all items")
  public List<ItemResponse> list() {
    return itemService.findAll().stream().map(ItemResponse::from).toList();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get an item by id")
  public ItemResponse get(@PathVariable Long id) {
    return ItemResponse.from(itemService.findById(id));
  }

  @PostMapping
  @Operation(summary = "Create an item")
  public ResponseEntity<ItemResponse> create(@Valid @RequestBody ItemRequest request) {
    ItemResponse body =
        ItemResponse.from(itemService.create(request.name(), request.description()));
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(body.id())
            .toUri();
    return ResponseEntity.created(location).body(body);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update an item")
  public ItemResponse update(@PathVariable Long id, @Valid @RequestBody ItemRequest request) {
    return ItemResponse.from(itemService.update(id, request.name(), request.description()));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Delete an item")
  public void delete(@PathVariable Long id) {
    itemService.delete(id);
  }
}
