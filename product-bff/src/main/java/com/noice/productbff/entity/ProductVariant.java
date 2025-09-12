package com.noice.productbff.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_variants")
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id",nullable = false,updatable = false,foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Product product;

    @OneToMany(orphanRemoval = true, mappedBy = "productVariant")
    private Set<ProductVariantAttribute> attributes;

    @NotBlank
    @Column(nullable = false)
    private String code;

    @Column(name = "weight_in_grams",nullable = false)
    @NotNull
    @Min(0)
    private Double weightInGrams;

    @NotNull
    @Min(0)
    @Column(name = "length_in_mm",nullable = false)
    private Double lengthInMM;

    @NotNull
    @Min(0)
    @Column(name = "width_in_mm",nullable = false)
    private Double widthInMM;

    @NotNull
    @Min(0)
    @Column(name = "height_in_mm",nullable = false)
    private Double heightInMM;

    @NotBlank
    @Column(nullable = false)
    private String countryOfOrigin;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private Instant createdDate;

    private Long createdBy;

    @UpdateTimestamp
    private Instant modifiedDate;

    private Long updatedBy;

    private String attributeHash;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ProductVariant that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
