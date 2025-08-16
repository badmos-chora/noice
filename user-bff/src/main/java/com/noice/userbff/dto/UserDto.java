package com.noice.userbff.dto;

import com.noice.userbff.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
public record UserDto(@NotBlank String password, @NotBlank String firstName, String lastName,
                      @Email @NotBlank String email,
                      @Size(min = 7, max = 20) @NotBlank String phoneNumber) implements Serializable {
}