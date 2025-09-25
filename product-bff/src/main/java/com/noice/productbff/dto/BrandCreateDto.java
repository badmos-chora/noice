package com.noice.productbff.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.noice.productbff.entity.Brand}
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class BrandCreateDto implements Serializable {
    @NotBlank
    String name;
    @NotBlank
    String description;
}