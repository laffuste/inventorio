package com.laffuste.inventorio.interfaces.rest.v1.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProductDto {

    @Schema(description = "Product ID")
    Long id;

    @Schema(description = "Name of the product")
    String name;

    @Schema(description = "Category of the product")
    Long categoryId;

    @Schema(description = "Remaining quantity in inventory")
    Integer quantity;

}
