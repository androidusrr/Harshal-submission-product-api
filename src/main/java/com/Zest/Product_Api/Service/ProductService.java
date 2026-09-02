package com.Zest.Product_Api.Service;

import com.Zest.Product_Api.Repository.ProductRepo;
import com.Zest.Product_Api.dto.ProductRequest;
import com.Zest.Product_Api.dto.ProductResponse;
import com.Zest.Product_Api.Entity.Item;
import com.Zest.Product_Api.Entity.Product;
import com.example.productapi.exception.ResourceNotFoundException;
import com.Zest.Product_Api.Repository.ItemRepo;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepo productRepository;
    private final ItemRepo itemRepository;

    public ProductService(
            ProductRepo productRepository,
            ItemRepo itemRepository) {

        this.productRepository = productRepository;
        this.itemRepository = itemRepository;
    }

    public ProductResponse createProduct(
            ProductRequest request,
            String username) {

        Product product = new Product();

        product.setProductName(request.getProductName());
        product.setCreatedBy(username);
        product.setCreatedOn(LocalDateTime.now());

        Product saved = productRepository.save(product);

        return convertToResponse(saved);
    }

    public Page<ProductResponse> getAllProducts(Pageable pageable) {

        return productRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id));

        return convertToResponse(product);
    }

    public ProductResponse updateProduct(
            Long id,
            ProductRequest request,
            String username) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id));

        product.setProductName(request.getProductName());
        product.setModifiedBy(username);
        product.setModifiedOn(LocalDateTime.now());

        Product updated = productRepository.save(product);

        return convertToResponse(updated);
    }

    public void deleteProduct(Long id) {

        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Product not found with id: " + id);
        }

        productRepository.deleteById(id);
    }

    public List<Item> getItems(Long productId) {

        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException(
                    "Product not found with id: " + productId);
        }

        return itemRepository.findByProductId(productId);
    }

    private ProductResponse convertToResponse(Product product) {

        return new ProductResponse(
                product.getId(),
                product.getProductName(),
                product.getCreatedBy(),
                product.getCreatedOn(),
                product.getModifiedBy(),
                product.getModifiedOn()
        );
    }
}