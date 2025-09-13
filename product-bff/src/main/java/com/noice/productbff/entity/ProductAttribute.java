package com.noice.productbff.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product_attributes")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotNull
    private String name;

    @OneToMany(orphanRemoval = true)
    @Builder.Default
    private Set<ProductAttributeValue> values= new HashSet<>();

    @Builder.Default
    private Boolean active=true;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ProductAttribute that)) return false;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
