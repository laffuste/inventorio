package com.laffuste.inventorio.domain.product;

import com.laffuste.inventorio.infrastructure.product.SearchProductCriteria;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {

    Page<Product> search(SearchProductCriteria criteria, int page, int size);

    Optional<Product> findById(long id);

    List<Product> findByCategoryId(long id);

    boolean exists(long id);

    Product create(Product product);

    Product update(Product product);

    void delete(long id);

}
