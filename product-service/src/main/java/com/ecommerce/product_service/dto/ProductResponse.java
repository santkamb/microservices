package com.ecommerce.product_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Product response DTO")
public class ProductResponse {

    @Schema(description = "Product unique identifier", example = "1")
    private Long id;

    @Schema(description = "Product name", example = "Laptop")
    private String name;

    @Schema(description = "Product description")
    private String description;

    @Schema(description = "Product price", example = "999.99")
    private BigDecimal price;

    @Schema(description = "Available quantity", example = "50")
    private Integer quantity;

    @Schema(description = "Product category", example = "Electronics")
    private String category;

    @Schema(description = "Stock Keeping Unit", example = "LAPTOP-001")
    private String sku;

    @Schema(description = "Is product active", example = "true")
    private Boolean isActive;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Product creation timestamp")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Product last update timestamp")
    private LocalDateTime updatedAt;
}
