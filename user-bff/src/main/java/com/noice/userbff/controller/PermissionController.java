package com.noice.userbff.controller;

import com.noice.userbff.service.repo.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
