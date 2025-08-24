package com.noice.userbff.service.impl;

import com.noice.userbff.projection.PermissionListProjection;
import com.noice.userbff.repository.PermissionRepository;
import com.noice.userbff.service.repo.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private PermissionRepository permissionRepository;

    @Override
    public List<PermissionListProjection> list() {
        return permissionRepository.findAllBy(PermissionListProjection.class);
    }
}
