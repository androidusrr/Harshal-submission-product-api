package com.Zest.Product_Api.Repository;

import com.Zest.Product_Api.Entity.*;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepo extends JpaRepository<Item, Long> {
    List<Item> findByProductId(Long productId);
}