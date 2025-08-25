package com.noice.userbff.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * DTO for {@link com.noice.userbff.entity.Permission}
 */
@AllArgsConstructor
@Getter
public class PermissionDto implements Serializable {
    private final Long id;
    @NotBlank(message = "permission name cant be black")
    private final String name;
    @NotBlank(message = "default roles missing")
    private final String defaultRoles;
}