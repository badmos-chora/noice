package com.noice.userbff.service.impl;

import com.noice.userbff.dto.PermissionDto;
import com.noice.userbff.entity.Permission;
import com.noice.userbff.entity.User;
import com.noice.userbff.exception.NoiceBusinessLogicException;
import com.noice.userbff.projection.PermissionListProjection;
import com.noice.userbff.repository.PermissionRepository;
import com.noice.userbff.repository.UserRepository;
import com.noice.userbff.service.repo.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private PermissionRepository permissionRepository;
    private UserRepository userRepository;

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
    public PermissionListProjection view(@NonNull Long id) {
        return permissionRepository.findById(id,PermissionListProjection.class)
                .orElseThrow(()-> new NoiceBusinessLogicException("Permission Not Found For Id "+id));
    }

    @Override
    @Transactional
    public void assign(@NonNull Long userId, Set<Long> ids) {

        User user = userRepository.findByIdWithPermissions(userId).orElseThrow(()-> new NoiceBusinessLogicException("User Not Found For Id "+userId));
        Set<Permission> permissions =user.getPermissions();
        if(ids==null || ids.isEmpty()){
            permissions.clear();
            return;
        }
        permissions.removeIf(permission -> !ids.contains(permission.getId()));
        Set<Long> permissionIds = permissions.stream().map(Permission::getId).collect(Collectors.toSet());
        List<Long> idsToAdd = ids.stream().filter(id -> !permissionIds.contains(id)).toList();
        List<Permission> permissionListToAdd = permissionRepository.findAllById(idsToAdd);
        String role = user.getRole().name();
        permissionListToAdd.forEach(permission -> {
            Set<String> roles = Arrays.stream(permission.getDefaultRoles().split(",")).collect(Collectors.toSet());
            if(!roles.contains(role)){
                throw new NoiceBusinessLogicException("Permission Not Found For Role "+role);
            }
            permissions.add(permission);
        });

    }


}
