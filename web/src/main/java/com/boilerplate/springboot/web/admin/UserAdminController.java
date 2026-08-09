package com.boilerplate.springboot.web.admin;

import com.boilerplate.springboot.domain.auth.AuthException;
import com.boilerplate.springboot.domain.auth.RoleRepository;
import com.boilerplate.springboot.domain.auth.UserAccount;
import com.boilerplate.springboot.domain.auth.UserAccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;
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
@RequestMapping("/admin/users")
public class UserAdminController {

  private final UserAccountService userAccountService;
  private final RoleRepository roleRepository;

  public UserAdminController(UserAccountService userAccountService, RoleRepository roleRepository) {
    this.userAccountService = userAccountService;
    this.roleRepository = roleRepository;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("pageTitle", "Users");
    model.addAttribute("users", userAccountService.findAll());
    return "admin/users/list";
  }

  @GetMapping("/new")
  public String createForm(Model model) {
    model.addAttribute("pageTitle", "New user");
    model.addAttribute("userForm", new UserForm());
    model.addAttribute("allRoles", roleRepository.findAll());
    return "admin/users/form";
  }

  @PostMapping
  public String create(
      @Valid @ModelAttribute("userForm") UserForm form,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("pageTitle", "New user");
      model.addAttribute("allRoles", roleRepository.findAll());
      return "admin/users/form";
    }
    if (form.getPassword() == null
        || form.getPassword().isBlank()
        || form.getPassword().length() < 6) {
      bindingResult.rejectValue("password", "error", "Password must be at least 6 characters");
      model.addAttribute("pageTitle", "New user");
      model.addAttribute("allRoles", roleRepository.findAll());
      return "admin/users/form";
    }
    try {
      userAccountService.create(
          form.getEmail(),
          form.getPassword(),
          form.getDisplayName(),
          new LinkedHashSet<>(form.getRoleNames()));
      redirectAttributes.addFlashAttribute("message", "User created");
      return "redirect:/admin/users";
    } catch (AuthException ex) {
      bindingResult.reject("error", ex.getMessage());
      model.addAttribute("pageTitle", "New user");
      model.addAttribute("allRoles", roleRepository.findAll());
      return "admin/users/form";
    }
  }

  @GetMapping("/{id}/edit")
  public String editForm(@PathVariable Long id, Model model) {
    UserAccount user = userAccountService.findById(id);
    UserForm form = new UserForm();
    form.setEmail(user.getEmail());
    form.setDisplayName(user.getDisplayName());
    form.setEnabled(user.isEnabled());
    form.setRoleNames(new LinkedHashSet<>(user.getRoleNames()));
    model.addAttribute("pageTitle", "Edit user");
    model.addAttribute("userForm", form);
    model.addAttribute("userId", id);
    model.addAttribute("allRoles", roleRepository.findAll());
    return "admin/users/form";
  }

  @PostMapping("/{id}")
  public String update(
      @PathVariable Long id,
      @Valid @ModelAttribute("userForm") UserForm form,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    // password optional on edit
    if (form.getPassword() == null || form.getPassword().isBlank()) {
      // clear password validation if we mark password not required on edit — validated in form with
      // Size only
    }
    if (bindingResult.hasErrors()
        && bindingResult.getFieldErrors().stream()
            .anyMatch(err -> !"password".equals(err.getField()))) {
      model.addAttribute("pageTitle", "Edit user");
      model.addAttribute("userId", id);
      model.addAttribute("allRoles", roleRepository.findAll());
      return "admin/users/form";
    }
    try {
      userAccountService.update(
          id, form.getDisplayName(), form.isEnabled(), new LinkedHashSet<>(form.getRoleNames()));
      if (form.getPassword() != null && !form.getPassword().isBlank()) {
        userAccountService.resetPassword(id, form.getPassword());
      }
      redirectAttributes.addFlashAttribute("message", "User updated");
      return "redirect:/admin/users";
    } catch (AuthException ex) {
      bindingResult.reject("error", ex.getMessage());
      model.addAttribute("pageTitle", "Edit user");
      model.addAttribute("userId", id);
      model.addAttribute("allRoles", roleRepository.findAll());
      return "admin/users/form";
    }
  }

  public static class UserForm {

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @Size(max = 100)
    private String password;

    @Size(max = 200)
    private String displayName;

    private boolean enabled = true;

    @NotEmpty private Set<String> roleNames = new LinkedHashSet<>();

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public String getDisplayName() {
      return displayName;
    }

    public void setDisplayName(String displayName) {
      this.displayName = displayName;
    }

    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    public Set<String> getRoleNames() {
      return roleNames;
    }

    public void setRoleNames(Set<String> roleNames) {
      this.roleNames = roleNames;
    }
  }
}
