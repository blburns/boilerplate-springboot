package com.boilerplate.springboot.web.item;

import com.boilerplate.springboot.domain.item.ItemNotFoundException;
import com.boilerplate.springboot.domain.item.ItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/items")
public class ItemPageController {

  private final ItemService itemService;

  public ItemPageController(ItemService itemService) {
    this.itemService = itemService;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("items", itemService.findAll());
    model.addAttribute("itemForm", new ItemForm());
    return "items/list";
  }

  @PostMapping
  public String create(
      @Valid @ModelAttribute("itemForm") ItemForm form,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("items", itemService.findAll());
      return "items/list";
    }
    itemService.create(form.getName(), form.getDescription());
    redirectAttributes.addFlashAttribute("message", "Item created");
    return "redirect:/items";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    try {
      itemService.delete(id);
      redirectAttributes.addFlashAttribute("message", "Item deleted");
    } catch (ItemNotFoundException ex) {
      redirectAttributes.addFlashAttribute("error", ex.getMessage());
    }
    return "redirect:/items";
  }

  public static class ItemForm {

    @NotBlank(message = "Name is required")
    @Size(max = 200)
    private String name;

    @Size(max = 2000)
    private String description;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }
  }
}
