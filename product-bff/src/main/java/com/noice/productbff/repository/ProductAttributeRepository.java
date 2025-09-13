package com.noice.productbff.repository;

import com.noice.productbff.entity.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttribute,Long> {
    <T> List<T> findAllBy(Class<T> type);

    @Transactional
    @Modifying
    @Query("update ProductAttribute p set p.active = ?1")
    void updateActiveStatus(Boolean active);
}
