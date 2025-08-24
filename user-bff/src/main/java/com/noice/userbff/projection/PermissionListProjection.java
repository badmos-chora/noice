package com.noice.userbff.projection;

/**
 * Projection for {@link com.noice.userbff.entity.Permission}
 */
public interface PermissionListProjection {
    Long getId();

    String getName();

    String getDefaultRoles();
}