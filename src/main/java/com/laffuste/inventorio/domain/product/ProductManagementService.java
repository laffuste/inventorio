package com.laffuste.inventorio.domain.product;

import com.laffuste.inventorio.domain.category.CategoryService;
import com.laffuste.inventorio.infrastructure.category.CategoryEntity;
import com.laffuste.inventorio.infrastructure.exception.ModelNotFoundException;
import com.laffuste.inventorio.infrastructure.product.ProductEntity;
import com.laffuste.inventorio.infrastructure.product.SearchProductCriteria;
import com.laffuste.inventorio.interfaces.config.InventorioConfig;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Service
@AllArgsConstructor
public class ProductManagementService {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final InventorioConfig inventorioConfig;

    public Page<Product> search(SearchProductCriteria criteria, Integer page) {
        return productService.search(criteria, defaultIfNull(page, 0), inventorioConfig.getProductPaging());
    }

    public Product findById(long id) throws ModelNotFoundException {
        return productService.findById(id).orElseThrow(() -> new ModelNotFoundException(Product.class, id));
    }

    public Product create(Product product) throws ModelNotFoundException {
        checkArgument(product.getId() == null, "Product id must be null when creating products");
        if (!categoryService.exists(product.getCategoryId())) {
            throw new ModelNotFoundException(CategoryEntity.class, product.getCategoryId());
        }
        return productService.create(product);
    }

    public Product update(Product product) throws ModelNotFoundException {
        checkArgument(product.getId() != null, "Product id must not be null when updating products");
        if (!productService.exists(product.getId())) {
            throw new ModelNotFoundException(ProductEntity.class, product.getId());
        }
        if (!categoryService.exists(product.getCategoryId())) {
            throw new ModelNotFoundException(CategoryEntity.class, product.getCategoryId());
        }
        return productService.update(product);
    }

    public void delete(long id) throws ModelNotFoundException {
        if (!productService.exists(id)) {
            throw new ModelNotFoundException(ProductEntity.class, id);
        }
        productService.delete(id);
    }

}
