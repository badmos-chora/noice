package com.noice.productbff.entity;

import com.noice.productbff.enums.ProductStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.Set;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true,nullable = false,length = 100)
    private String slug;

    @Enumerated(EnumType.STRING)
    @NotBlank
    @Column(unique = true,nullable = false)
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

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private Instant createdDate;

    private Long createdBy;

    @UpdateTimestamp
    private Instant modifiedDate;

    private Long updatedBy;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String,Object> seo;

    @OneToMany(orphanRemoval = true,mappedBy = "product")
    private Set<ProductVariant> variants;



    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
