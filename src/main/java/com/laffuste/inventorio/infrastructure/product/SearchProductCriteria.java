package com.laffuste.inventorio.infrastructure.product;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SearchProductCriteria {

    String name;
    Long categoryId;
    Boolean isAvailable;

}
