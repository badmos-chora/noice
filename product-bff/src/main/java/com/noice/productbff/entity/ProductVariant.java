package com.noice.productbff.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SoftDelete
@Table(name = "product_variants")
public class ProductVariant extends AuditableBaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id",nullable = false,updatable = false,foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Product product;

    @OneToMany(orphanRemoval = true, mappedBy = "productVariant")
    private Set<ProductVariantAttribute> attributes;

    @NotBlank
    @Column(nullable = false,unique = true,updatable = false)
    @NaturalId
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

    private String attributeHash;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ProductVariant that = (ProductVariant) o;
        return getCode() != null && Objects.equals(getCode(), that.getCode());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(code);
    }
}
