package com.dlvi.studentmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record StudentRequestDto(@NotBlank
                                @NotNull(message = "The name cannot be void")
                                String name,
                                @NotBlank
                                @Email(message = "The email cannot be void")
                                String email,
                                @NotNull(message = "The date is mandatory")
                                LocalDate birthDate,
                                String sex) {
}
