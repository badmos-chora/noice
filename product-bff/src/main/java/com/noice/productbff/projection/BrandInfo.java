package com.noice.productbff.projection;

/**
 * Projection for {@link com.noice.productbff.entity.Brand}
 */
public interface BrandInfo {
    Long getId();

    String getSlug();

    String getName();

    String getDescription();
}