package com.noice.userbff.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * DTO for {@link com.noice.userbff.entity.User}
 */
@AllArgsConstructor
@Getter
public class UserModel implements Serializable {
    private final Long id;
    @NotBlank
    private final String firstName;
    private final String lastName;
    @NotBlank
    private final String email;
    @Size(min = 7, max = 20)
    @NotBlank
    private final String phoneNumber;
}