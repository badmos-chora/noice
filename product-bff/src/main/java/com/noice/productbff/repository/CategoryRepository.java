package com.noice.productbff.repository;

import com.noice.productbff.entity.Category;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    boolean existsBySlugIgnoreCase(String slug);
    <T> Optional<T>  findById(Long id, Class<T> type);

}