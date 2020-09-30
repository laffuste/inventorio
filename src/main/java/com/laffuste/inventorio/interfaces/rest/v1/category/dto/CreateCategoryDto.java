package com.laffuste.inventorio.interfaces.rest.v1.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@Builder
public class CreateCategoryDto {

    @Schema(description = "Name of the category")
    @NotBlank
    String name;

    @Schema(description = "Parent id for subcategories. Leave blank for top categories.")
    Long parentId;

}
