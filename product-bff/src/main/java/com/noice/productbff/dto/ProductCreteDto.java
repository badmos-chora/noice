package com.noice.productbff.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.noice.productbff.entity.Product;
import com.noice.productbff.enums.Department;
import com.noice.productbff.enums.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link Product}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductCreteDto(@NotBlank(message = "product slug required") String slug,
                              @NotNull(message = "product status required") ProductStatus status, Long brandId,
                              @NotBlank(message = "product title required") String title,
                              @Size(max = 500) String subTitle,
                              @NotBlank(message = "description is required") String description,
                              @NotNull(message = "department is required") Department department,
                              Set<Long> categoryIds
                              ) implements Serializable {
}