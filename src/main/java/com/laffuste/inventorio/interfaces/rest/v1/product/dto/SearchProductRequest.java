package com.laffuste.inventorio.interfaces.rest.v1.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SearchProductRequest {

    String name;
    Long categoryId;
    Boolean isAvailable;

    Integer page;

}
