package com.noice.userbff.projection;

/**
 * Projection for {@link com.noice.userbff.entity.User}
 */
public interface UserProfileProjection {
    Long getId();

    String getFirstName();

    String getLastName();

    String getEmail();

    String getPhoneNumber();
}