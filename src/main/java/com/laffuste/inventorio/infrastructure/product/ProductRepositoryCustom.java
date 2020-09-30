package com.laffuste.inventorio.infrastructure.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductRepositoryCustom {

    Page<ProductEntity> search(SearchProductCriteria criteria, Pageable pageable);

}