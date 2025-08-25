package com.noice.userbff.service.impl;

import com.noice.userbff.dto.PermissionDto;
import com.noice.userbff.entity.Permission;
import com.noice.userbff.exception.NoiceBusinessLogicException;
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

    @Override
    public void add(PermissionDto permissionDto) {
        Permission.PermissionBuilder builder = Permission.builder();
        builder.name(permissionDto.getName())
                .defaultRoles(permissionDto.getDefaultRoles());
        permissionRepository.save(builder.build());
    }

    @Override
    public PermissionListProjection view(Long id) {
        return permissionRepository.findById(id,PermissionListProjection.class)
                .orElseThrow(()-> new NoiceBusinessLogicException("Permission Not Found For Id "+id));
    }


}
