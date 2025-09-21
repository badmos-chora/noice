package com.noice.productbff.projection;

import com.noice.productbff.entity.Category;

import java.util.List;

/**
 * Projection for {@link com.noice.productbff.entity.Category}
 */
public interface CategoryInfoProjection {
    Long getId();

    String getName();

    String getSlug();

    CategoryInfoProjection getParent();

    List<CategoryInfoProjection> getChildren();
}