package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.ProductListResponse;
import com.ecommerce.product_service.dto.ProductRequest;
import com.ecommerce.product_service.dto.ProductResponse;
import com.ecommerce.product_service.entity.Product;
import com.ecommerce.product_service.exception.ResourceNotFoundException;
import com.ecommerce.product_service.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Create a new product
     */
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating new product with name: {}", request.getName());

        if (productRepository.existsBySku(request.getSku())) {
            log.warn("Product with SKU {} already exists", request.getSku());
            throw new RuntimeException("Product with SKU " + request.getSku() + " already exists");
        }

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .category(request.getCategory())
                .sku(request.getSku())
                .isActive(true)
                .build();

        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with ID: {}", savedProduct.getId());
        return mapToResponse(savedProduct);
    }

    /**
     * Get product by ID
     */
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        log.info("Fetching product with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found with ID: {}", id);
                    return new ResourceNotFoundException("Product not found with ID: " + id);
                });
        return mapToResponse(product);
    }

    /**
     * Get all products with pagination
     */
    @Transactional(readOnly = true)
    public ProductListResponse getAllProducts(Pageable pageable) {
        log.info("Fetching all products with page: {}", pageable.getPageNumber());
        Page<Product> products = productRepository.findByIsActiveTrue(pageable);
        return mapToPageResponse(products);
    }

    /**
     * Search products by name
     */
    @Transactional(readOnly = true)
    public ProductListResponse searchByName(String name, Pageable pageable) {
        log.info("Searching products by name: {}", name);
        Page<Product> products = productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name, pageable);
        return mapToPageResponse(products);
    }

    /**
     * Get products by category
     */
    @Transactional(readOnly = true)
    public ProductListResponse getProductsByCategory(String category, Pageable pageable) {
        log.info("Fetching products by category: {}", category);
        Page<Product> products = productRepository.findByCategoryIgnoreCaseAndIsActiveTrue(category, pageable);
        return mapToPageResponse(products);
    }

    /**
     * Find products by price range
     */
    @Transactional(readOnly = true)
    public ProductListResponse getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        log.info("Fetching products by price range: {} - {}", minPrice, maxPrice);
        Page<Product> products = productRepository.findByPriceRange(minPrice, maxPrice, pageable);
        return mapToPageResponse(products);
    }

    /**
     * Complex search
     */
    @Transactional(readOnly = true)
    public ProductListResponse search(String searchTerm, String category, Pageable pageable) {
        log.info("Searching products with term: {} and category: {}", searchTerm, category);
        Page<Product> products = productRepository.searchByTermAndCategory(searchTerm, category, pageable);
        return mapToPageResponse(products);
    }

    /**
     * Update product
     */
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        log.info("Updating product with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found with ID: {}", id);
                    return new ResourceNotFoundException("Product not found with ID: " + id);
                });

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setCategory(request.getCategory());
        product.setSku(request.getSku());

        Product updatedProduct = productRepository.save(product);
        log.info("Product updated successfully with ID: {}", id);
        return mapToResponse(updatedProduct);
    }

    /**
     * Delete product (soft delete)
     */
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found with ID: {}", id);
                    return new ResourceNotFoundException("Product not found with ID: " + id);
                });

        product.setIsActive(false);
        productRepository.save(product);
        log.info("Product deleted successfully with ID: {}", id);
    }

    /**
     * Get low stock products
     */
    @Transactional(readOnly = true)
    public ProductListResponse getLowStockProducts(Integer threshold, Pageable pageable) {
        log.info("Fetching low stock products with threshold: {}", threshold);
        Page<Product> products = productRepository.findLowStockProducts(threshold, pageable);
        return mapToPageResponse(products);
    }

    /**
     * Update product quantity
     */
    public ProductResponse updateProductQuantity(Long id, Integer quantity) {
        log.info("Updating quantity for product ID: {} to: {}", id, quantity);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found with ID: {}", id);
                    return new ResourceNotFoundException("Product not found with ID: " + id);
                });

        product.setQuantity(quantity);
        Product updatedProduct = productRepository.save(product);
        log.info("Product quantity updated successfully for ID: {}", id);
        return mapToResponse(updatedProduct);
    }

    /**
     * Map Product entity to ProductResponse DTO
     */
    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .category(product.getCategory())
                .sku(product.getSku())
                .isActive(product.getIsActive())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    /**
     * Map Page<Product> to ProductListResponse DTO
     */
    private ProductListResponse mapToPageResponse(Page<Product> page) {
        return ProductListResponse.builder()
                .content(page.getContent().stream()
                        .map(this::mapToResponse)
                        .toList())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .isLast(page.isLast())
                .build();
    }
}
