package com.dlvi.studentmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StudentPatchDto(@NotBlank
                              @NotNull(message = "The name cannot be void")
                              String name,
                              @NotBlank
                              @Email(message = "The email cannot be void")
                              String email) {
}
