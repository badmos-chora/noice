package com.noice.userbff.service.repo;

import com.noice.userbff.dto.UserDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public interface AccountServices {
    void registerUser(@Valid UserDto userDto);

    String authenticate(@NotBlank String username, @NotBlank String password);
}
