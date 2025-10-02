package com.noice.productbff.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.noice.productbff.entity.Category;
import com.noice.productbff.enums.Department;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link Category}
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Category data transfer object")
public record CategoryDto(@Schema(description = "name of the category",requiredMode = Schema.RequiredMode.REQUIRED,example = "PHONE") @NotBlank(message = "Category name can't be black") String name,
                         @Schema(description = "parent category id if present", requiredMode = Schema.RequiredMode.NOT_REQUIRED) Long parentId,
                          @Schema(description = "department of the category",requiredMode = Schema.RequiredMode.REQUIRED, examples = {"ELECTRONICS", "FASHION"}) Department department) implements Serializable {
}