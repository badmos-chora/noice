package com.noice.userbff.service.repo;

import com.noice.userbff.dto.PermissionDto;
import com.noice.userbff.projection.PermissionListProjection;

import java.util.List;

public interface PermissionService {
    List<PermissionListProjection> list();

    void add(PermissionDto permissionDto);

    PermissionListProjection view(Long id);
}
