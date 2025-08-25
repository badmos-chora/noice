package com.noice.userbff.controller;

import com.noice.userbff.dto.PermissionDto;
import com.noice.userbff.service.repo.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> create(@RequestBody PermissionDto permissionDto){
        permissionService.add(permissionDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('permission.view')")
    public ResponseEntity<?> view(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.view(id));
    }

}
