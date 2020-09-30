package com.laffuste.inventorio.domain.product;

import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@Builder
@With
public class Product {

    Long id;
    String name;
    Long categoryId;
    int quantity;

}
