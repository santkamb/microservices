package com.ecommerce.product_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Paginated product list response")
public class ProductListResponse {

    @Schema(description = "List of products")
    private List<ProductResponse> content;

    @Schema(description = "Current page number (0-indexed)", example = "0")
    private Integer pageNumber;

    @Schema(description = "Page size", example = "10")
    private Integer pageSize;

    @Schema(description = "Total number of elements", example = "100")
    private Long totalElements;

    @Schema(description = "Total number of pages", example = "10")
    private Integer totalPages;

    @Schema(description = "Is this the last page", example = "false")
    private Boolean isLast;
}
