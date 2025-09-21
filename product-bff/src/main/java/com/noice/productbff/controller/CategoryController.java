package com.noice.productbff.controller;

import com.noice.productbff.dto.CategoryDto;
import com.noice.productbff.service.interfaces.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/category")
@AllArgsConstructor
@Validated
public class CategoryController {
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> list(
            @RequestParam(name = "page",defaultValue = "0") @Min(value = -1, message = "min page no = -1") Integer page,
            @RequestParam(name = "size",defaultValue = "100") @Min(value = 1,message = "min page size = 1") Integer size,
            @RequestParam(name = "sortBy",defaultValue = "id") @NotBlank(message = "sort by can be empty") String sortBy,
            @RequestParam( name = "sortDirection", defaultValue = "ASC") @Pattern(regexp = "ASC|DESC", message = "Sort direction must be 'ASC' or 'DESC'") String sortDirection) {
        return ResponseEntity.ok(categoryService.list(page,size,sortBy,sortDirection));
    }

    @PostMapping
    private ResponseEntity<?> create(@RequestBody @Valid CategoryDto category) {
        categoryService.create(category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> get(@NotNull @PathVariable Long id) {
        return ResponseEntity.ok(categoryService.get(id));
    }

    @PutMapping("{id}")
    private ResponseEntity<?> update(@RequestBody @Valid CategoryDto category, @PathVariable @NotNull Long id) {
        categoryService.update(id,category);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@NotNull @PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
