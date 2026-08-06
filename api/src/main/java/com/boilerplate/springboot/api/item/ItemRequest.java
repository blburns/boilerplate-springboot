package com.boilerplate.springboot.api.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ItemRequest(
    @NotBlank(message = "name is required") @Size(max = 200) String name,
    @Size(max = 2000) String description) {}
