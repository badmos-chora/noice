package com.noice.productbff.controller;

import com.noice.productbff.dto.BrandCreateDto;
import com.noice.productbff.service.interfaces.BrandServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/brand",produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Tag(name = "Brand", description = "Manage brands")
public class BrandController {

    private BrandServices brandServices;

    @GetMapping
    public ResponseEntity<?> list(
            @RequestParam(name = "page",defaultValue = "0") @Min(value = -1, message = "min page no = -1") Integer page,
            @RequestParam(name = "size",defaultValue = "100") @Min(value = 1,message = "min page size = 1") Integer size,
            @RequestParam(name = "sortBy",defaultValue = "id") @NotBlank(message = "sort by can be empty") String sortBy,
            @RequestParam( name = "sortDirection", defaultValue = "ASC") @Pattern(regexp = "ASC|DESC", message = "Sort direction must be 'ASC' or 'DESC'") String sortDirection) {
        return ResponseEntity.ok(brandServices.list(page,size,sortBy,sortDirection));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody BrandCreateDto brandInfo) {
        brandServices.create(brandInfo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(brandServices.getById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @Valid @RequestBody BrandCreateDto brandInfo) {
        brandServices.update(id, brandInfo);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        brandServices.delete(id);
        return ResponseEntity.ok().build();
    }

}
