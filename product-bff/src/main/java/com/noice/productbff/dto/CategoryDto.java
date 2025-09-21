package com.noice.productbff.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.noice.productbff.entity.Category;
import com.noice.productbff.enums.Department;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link Category}
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public record CategoryDto(@NotBlank(message = "Category name can't be black") String name,
                          Long parentId, Department department) implements Serializable {
}