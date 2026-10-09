package com.ecommerce.product_service.repository;

import com.ecommerce.product_service.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find product by name
     */
    Page<Product> findByNameContainingIgnoreCaseAndIsActiveTrue(String name, Pageable pageable);

    /**
     * Find products by category
     */
    Page<Product> findByCategoryIgnoreCaseAndIsActiveTrue(String category, Pageable pageable);

    /**
     * Find products by price range
     */
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice AND p.isActive = true")
    Page<Product> findByPriceRange(
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable
    );

    /**
     * Find all active products
     */
    Page<Product> findByIsActiveTrue(Pageable pageable);

    /**
     * Find product by SKU
     */
    Optional<Product> findBySku(String sku);

    /**
     * Complex search by name and category
     */
    @Query("SELECT p FROM Product p WHERE " +
            "(p.name LIKE CONCAT('%', :searchTerm, '%') OR p.description LIKE CONCAT('%', :searchTerm, '%')) " +
            "AND p.category = :category AND p.isActive = true")
    Page<Product> searchByTermAndCategory(
            @Param("searchTerm") String searchTerm,
            @Param("category") String category,
            Pageable pageable
    );

    /**
     * Check if product exists by SKU
     */
    boolean existsBySku(String sku);

    /**
     * Get low stock products
     */
    @Query("SELECT p FROM Product p WHERE p.quantity < :threshold AND p.isActive = true")
    Page<Product> findLowStockProducts(
            @Param("threshold") Integer threshold,
            Pageable pageable
    );
}
