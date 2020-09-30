package com.laffuste.inventorio.interfaces.rest.v1.product.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@Builder
public class CreateProductDto {

    @NotBlank
    String name;

    @NotNull
    Long categoryId;

    @NotNull
    Integer quantity;

}
