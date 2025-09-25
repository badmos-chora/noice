package com.noice.productbff.service.impl;

import com.noice.productbff.dto.BrandCreateDto;
import com.noice.productbff.entity.Brand;
import com.noice.productbff.exception.NoiceBusinessLogicException;
import com.noice.productbff.projection.BrandInfo;
import com.noice.productbff.repository.BrandRepository;
import com.noice.productbff.service.interfaces.BrandServices;
import com.noice.productbff.utils.GeneralUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class BrandServicesImpl implements BrandServices {
    private BrandRepository brandRepository;
    private static final Set<String> ALLOWED_SORTS = Set.of("id", "name", "slug", "createdDate", "lastModifiedDate");

    @Override
    public List<BrandInfo> list(Integer page, Integer size, String sortBy, String sortDirection) {
        if (!ALLOWED_SORTS.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }
        Pageable pageRequest = GeneralUtils.getPageable(page, size, sortDirection, sortBy);
        if(page == -1){
            return brandRepository.findBy(Specification.unrestricted(), query-> query.as(BrandInfo.class).sortBy(pageRequest.getSort())).all();
        }
        return brandRepository.findBy(Specification.unrestricted(), query-> query.as(BrandInfo.class).page(pageRequest)).getContent();
    }

    @Override
    public void create(BrandCreateDto brandInfo) {
        String slug = GeneralUtils.slugify(brandInfo.getName());
        if(brandRepository.existsBySlugIgnoreCase(slug)) throw new NoiceBusinessLogicException("Slug already exists" +slug);
        Brand brand = new Brand();
        brand.setName(brandInfo.getName());
        brand.setSlug(slug);
        brand.setDescription(brandInfo.getDescription());
        brandRepository.save(brand);
    }

    @Override
    public BrandInfo getById(Long id) {
        return brandRepository.findById(id,BrandInfo.class).orElseThrow(()->new NoiceBusinessLogicException("Brand not found with id: " + id));
    }

    @Override
    @Transactional
    public void update(Long id, BrandCreateDto brandInfo) {
        Brand brand = brandRepository.findById(id).orElseThrow(()->new NoiceBusinessLogicException("Brand not found with id: " + id));
        brand.setName(brandInfo.getName());
        String slug = GeneralUtils.slugify(brandInfo.getName());
        if(brandRepository.existsBySlugIgnoreCase(slug)) throw new NoiceBusinessLogicException("Slug already exists" +slug);
        brand.setSlug(slug);
        brand.setDescription(brandInfo.getDescription());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if(brandRepository.existsById(id)) throw new NoiceBusinessLogicException("Brand not found with id: " + id);
        brandRepository.deleteById(id);
    }
}
