package com.noice.productbff.service.impl;

import com.noice.productbff.dto.CategoryDto;
import com.noice.productbff.entity.Category;
import com.noice.productbff.exception.NoiceBusinessLogicException;
import com.noice.productbff.projection.CategoryInfoProjection;
import com.noice.productbff.repository.CategoryRepository;
import com.noice.productbff.service.interfaces.CategoryService;
import com.noice.productbff.utils.GeneralUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Set;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private static final Set<String> ALLOWED_SORTS = Set.of("id", "name", "slug", "createdDate", "lastModifiedDate");


    @Override
    public Page<CategoryInfoProjection> list(Integer page, Integer size, String sortBy, String sortDirection)
    {
        if (!ALLOWED_SORTS.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }
        Pageable pageRequest = GeneralUtils.getPageable(page, size, sortDirection, sortBy);
        return categoryRepository.findBy(Specification.unrestricted(), f -> f.as(CategoryInfoProjection.class).sortBy(pageRequest.getSort()).page(pageRequest));
    }

    @Override
    public void create(CategoryDto category) {
        Category categoryEntity = new Category();
        categoryEntity.setName(category.name());
        categoryEntity.setDepartment(category.department());
        String slug = GeneralUtils.slugify(category.name());
        boolean slugExists = categoryRepository.existsBySlugIgnoreCase(slug);
        if (slugExists) throw new NoiceBusinessLogicException("Category slug already exists");
        categoryEntity.setSlug(slug);
        if(category.parentId() != null) categoryEntity.setParent(categoryRepository.getReferenceById(category.parentId()));
        categoryRepository.save(categoryEntity);
    }

    @Override
    @Transactional
    public void update(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NoiceBusinessLogicException("No category with id: "+id));
        category.setName(categoryDto.name());
        String slug = GeneralUtils.slugify(categoryDto.name());
        category.setDepartment(categoryDto.department());
        boolean slugExists = categoryRepository.existsBySlugIgnoreCase(slug);
        if (slugExists) throw new NoiceBusinessLogicException("Category slug already exists");
        category.setSlug(slug);
        if(categoryDto.parentId() != null) category.setParent(categoryRepository.getReferenceById(categoryDto.parentId()));
        else category.setParent(null);
    }

    @Override
    public CategoryInfoProjection get(Long id) {
        return categoryRepository.findById(id,CategoryInfoProjection.class).orElseThrow(() -> new NoiceBusinessLogicException("No category with id: "+id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
