package com.noice.productbff.repository;

import com.noice.productbff.entity.Product;
import org.springframework.data.repository.Repository;

public interface ProductRepository extends Repository<Product, Long> {
}