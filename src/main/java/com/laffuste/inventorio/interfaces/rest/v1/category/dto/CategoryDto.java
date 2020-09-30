package com.laffuste.inventorio.interfaces.rest.v1.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CategoryDto {

    @Schema(description = "Id for the category")
    Long id;

    @Schema(description = "Name of the category")
    String name;

    @Schema(description = "Parent category ID of this (sub) category")
    Long parentId;

}
