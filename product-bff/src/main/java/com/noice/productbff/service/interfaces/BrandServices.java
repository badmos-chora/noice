package com.noice.productbff.service.interfaces;


import com.noice.productbff.dto.BrandCreateDto;
import com.noice.productbff.projection.BrandInfo;
import jakarta.validation.Valid;

import java.util.List;

public interface BrandServices {
    List<BrandInfo> list(Integer page, Integer size, String sortBy, String sortDirection);

    void create(@Valid  BrandCreateDto brandInfo);

    BrandInfo getById(Long id);

    void update(Long id, @Valid BrandCreateDto brandInfo);

    void delete(Long id);
}
