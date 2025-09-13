package com.noice.productbff.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
public class AttributeDefinitionRequest {
    @NotBlank
    private String name;
    @Builder.Default
    private Set<String> values = new HashSet<>();
}
