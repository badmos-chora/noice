package com.noice.productbff.repository;

import com.noice.productbff.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long>, JpaSpecificationExecutor<Brand> {
    boolean existsBySlugIgnoreCase(String slug);
    <T> Optional<T> findById(Long id, Class<T> type);
}