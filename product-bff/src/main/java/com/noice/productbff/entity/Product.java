package com.noice.productbff.entity;

import com.noice.productbff.enums.ProductStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@SoftDelete
public class Product  extends AuditableBaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true,nullable = false,length = 100)
    @NaturalId(mutable = true)
    private String slug;

    @Enumerated(EnumType.STRING)
    @NotBlank
    @Column(nullable = false)
    private ProductStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    private Brand brand;

    @NotNull
    @Column(nullable = false,length = 300)
    private String title;

    @Column(length = 500)
    @Size(max = 500)
    private String subTitle;

    @Lob
    @JdbcTypeCode(SqlTypes.LONG32VARCHAR)
    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String,Object> seo;

    @OneToMany(orphanRemoval = true,mappedBy = "product", fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})
    @Builder.Default
    private Set<ProductVariant> variants = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},fetch =  FetchType.LAZY)
    @Builder.Default
    @JoinTable(name = "product_category_mapping", uniqueConstraints = @UniqueConstraint(name = "product_category_unique_constraint",columnNames = {"category_id","product_id" }),
            joinColumns = @JoinColumn(name = "category_id",nullable = false), inverseJoinColumns = @JoinColumn(name = "product_id"),
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT),inverseForeignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Set<Category> categories = new HashSet<>();


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Product product = (Product) o;
        return getSlug() != null && Objects.equals(getSlug(), product.getSlug());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(slug);
    }
}
