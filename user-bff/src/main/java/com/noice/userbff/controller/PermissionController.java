package com.noice.userbff.controller;

import com.noice.userbff.dto.PermissionDto;
import com.noice.userbff.service.repo.PermissionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/permission")
@AllArgsConstructor
public class PermissionController {

    private PermissionService permissionService;

    @GetMapping({"","/"})
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('permission.list')")
    public ResponseEntity<?> list(){
        return ResponseEntity.ok().body(permissionService.list());
    }

    @PostMapping({"","/"})
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('permission.create')")
    public ResponseEntity<?> create(@Valid @RequestBody PermissionDto permissionDto){
        permissionService.add(permissionDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('permission.view')")
    public ResponseEntity<?> view(@PathVariable @NotNull Long id){
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.view(id));
    }

    @PostMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('permission.assign')")
    public ResponseEntity<?> assign(@RequestBody Set<Long> ids, @PathVariable @NotNull Long userId){
        permissionService.assign(userId,ids);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
