package com.Zest.Product_Api.Repository;

import com.Zest.Product_Api.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo
        extends JpaRepository<Product, Long> {
}