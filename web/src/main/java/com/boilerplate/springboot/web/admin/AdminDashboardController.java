package com.boilerplate.springboot.web.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

  @GetMapping({"", "/"})
  public String dashboard(Model model) {
    model.addAttribute("pageTitle", "Dashboard");
    return "admin/dashboard";
  }
}
