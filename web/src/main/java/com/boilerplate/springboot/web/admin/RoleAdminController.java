package com.boilerplate.springboot.web.admin;

import com.boilerplate.springboot.domain.auth.RoleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/roles")
public class RoleAdminController {

  private final RoleRepository roleRepository;

  public RoleAdminController(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("pageTitle", "Roles");
    model.addAttribute("roles", roleRepository.findAll());
    return "admin/roles/list";
  }
}
