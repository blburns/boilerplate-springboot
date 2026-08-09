package com.boilerplate.springboot.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "com.boilerplate.springboot.web")
public class WebMvcAdvice {

  @ModelAttribute("currentUri")
  public String currentUri(HttpServletRequest request) {
    return request.getRequestURI();
  }
}
