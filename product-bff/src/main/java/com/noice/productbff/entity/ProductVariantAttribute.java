package com.noice.productbff.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_variant_attributes", uniqueConstraints = @UniqueConstraint(name = "product_variant_product_attribute_value_unique",columnNames = {"product_variant_id","product_attribute_value_id"}))
@Builder
public class ProductVariantAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_variant_id",nullable = false,updatable = false)
    private ProductVariant productVariant;


    @ManyToOne(optional = false)
    @JoinColumn(name = "product_attribute_value_id",nullable = false,updatable = false)
    private ProductAttributeValue productAttributeValue;


    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ProductVariantAttribute that)) return false;
        return id.equals(that.id) && productAttributeValue.equals(that.productAttributeValue) && productVariant.equals(that.productVariant);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
