package com.noice.productbff.controller;

import com.noice.productbff.dto.CategoryDto;
import com.noice.productbff.projection.CategoryInfoProjection;
import com.noice.productbff.service.interfaces.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(name = "/category",produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Validated
@Tag(name = "Categories", description = "Manage product categories")
public class CategoryController {
    private CategoryService categoryService;

    @GetMapping
    @PreAuthorize("hasAuthority('category.list')")
    @Operation(summary = "Get category list",
    description = "Requires authority: `category.list`. Returns a paginated list of categories. Use `page = -1` to fetch all (unpaged). ",
    parameters = {
            @Parameter(name = "page",description = "zero-based page index use -1 to fetch all (unpaged)", schema = @Schema(minimum = "-1", defaultValue = "0")),
            @Parameter(name = "size", description = "page size", schema = @Schema(minimum = "1", maximum = "200", defaultValue = "100")),
            @Parameter(name = "sortBy", description = "property to sort by", schema = @Schema(allowableValues = {"id", "name", "slug", "createdDate", "lastModifiedDate"}, defaultValue = "id")),
            @Parameter(name = "sortDirection", description = "Sort direction", schema = @Schema(allowableValues = {"ASC", "DESC"}, defaultValue = "ASC"))
    })
    public ResponseEntity<Page<CategoryInfoProjection>> list(
            @RequestParam(name = "page",defaultValue = "0") @Min(value = -1, message = "min page no = -1") Integer page,
            @RequestParam(name = "size",defaultValue = "100") @Min(value = 1,message = "min page size = 1") @Max(value = 200,message = "page size can't be more than 200") Integer size,
            @RequestParam(name = "sortBy",defaultValue = "id") String sortBy,
            @RequestParam( name = "sortDirection", defaultValue = "ASC") @Pattern(regexp = "ASC|DESC", message = "Sort direction must be 'ASC' or 'DESC'") String sortDirection) {
        return ResponseEntity.ok(categoryService.list(page,size,sortBy,sortDirection));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('category.create')")
    @Operation(summary = "Create new category",
    description = "Requires authority: 'category.create'")
    @ApiResponse(responseCode = "201", description = "Category created successfully")
    public ResponseEntity<Void> create(@RequestBody @Valid CategoryDto category) {
        categoryService.create(category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('category.get')")
    @Operation(summary = "Get category from id",
            description = "Requires authority: 'category.get'",
    parameters = {@Parameter(name = "id", description = "id of category to get", required = true)})
    public ResponseEntity<CategoryInfoProjection> get(@NotNull @PathVariable Long id) {
        return ResponseEntity.ok(categoryService.get(id));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('category.put')")
    @Operation(summary = "Update category with id",
            description = "Requires authority: 'category.put'",
            parameters = {@Parameter(name = "id", description = "id of category to update", required = true)})
    @ApiResponse(responseCode = "204", description = "Category updated")
    public ResponseEntity<Void> update(@RequestBody @Valid CategoryDto category, @PathVariable @NotNull Long id) {
        categoryService.update(id,category);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('category.delete')")
    @Operation(summary = "Delete category with id",
            description = "Requires authority: 'category.delete'",
            parameters = {@Parameter(name = "id", description = "id of category to delete", required = true)})
    @ApiResponse(responseCode = "200", description = "Category delete")
    public ResponseEntity<?> delete(@NotNull @PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
