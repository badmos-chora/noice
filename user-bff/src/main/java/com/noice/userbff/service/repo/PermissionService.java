package com.noice.userbff.service.repo;

import com.noice.userbff.projection.PermissionListProjection;

import java.util.List;

public interface PermissionService {
    List<PermissionListProjection> list();
}
