package com.noice.media.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "assets")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "asset_type", discriminatorType = DiscriminatorType.STRING, length = 32)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public abstract class Asset extends AuditableBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)                 private String mimeType;
    @Column(nullable = false)                 private long sizeBytes;
    @Column(nullable = false)                 private String bucket;
    @Column(nullable = false, unique = true)  private String storageKey;
    @Column(nullable = false, length = 64)    private String sha256Hex;
}
