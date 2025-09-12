package com.noice.productbff.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_attribute_values")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotNull
    private String value;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private ProductAttribute attribute;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ProductAttributeValue that)) return false;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
