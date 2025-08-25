package com.noice.userbff.service.repo;

import com.noice.userbff.dto.PermissionDto;
import com.noice.userbff.projection.PermissionListProjection;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Set;

public interface PermissionService {
    List<PermissionListProjection> list();

    void add(PermissionDto permissionDto);

    PermissionListProjection view(@NonNull  Long id);

    void assign(@NonNull Long userId, Set<Long> ids);
}
