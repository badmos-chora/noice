package com.noice.productbff.service.interfaces;


import com.noice.productbff.dto.CategoryDto;
import com.noice.productbff.projection.CategoryInfoProjection;
import org.springframework.data.domain.Page;




public interface CategoryService {
    Page<CategoryInfoProjection> list(Integer page, Integer size, String sortBy, String sortDirection);

    void create(CategoryDto category);

    void update(Long id, CategoryDto category);

    CategoryInfoProjection get(Long id);

    void delete(Long id);
}
