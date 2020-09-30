package com.laffuste.inventorio.infrastructure.product;

import com.laffuste.inventorio.domain.product.Product;
import com.laffuste.inventorio.domain.product.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final ProductEntityMapper productEntityMapper = ProductEntityMapper.MAPPER;

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<Product> search(SearchProductCriteria criteria, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ProductEntity> entities = productRepository.search(criteria, pageRequest);
        return entities.map(productEntityMapper::toModel);
    }

    @Transactional(readOnly = true)
    public Optional<Product> findById(long id) {
        return productRepository.findById(id).map(productEntityMapper::toModel);
    }

    @Transactional(readOnly = true)
    public List<Product> findByCategoryId(long id) {
        List<ProductEntity> entities = productRepository.findAllByCategoryId(id);
        return productEntityMapper.toModels(entities);
    }

    @Transactional(readOnly = true)
    public boolean exists(long id) {
        return productRepository.existsById(id);
    }


    public Product create(Product product) {
        ProductEntity productEntity = productEntityMapper.toEntity(product);
        ProductEntity savedCategory = productRepository.save(productEntity);
        return productEntityMapper.toModel(savedCategory);
    }

    public Product update(Product product) {
        ProductEntity productEntity = productEntityMapper.toEntity(product);
        ProductEntity updatedProduct = productRepository.save(productEntity);
        return productEntityMapper.toModel(updatedProduct);
    }

    public void delete(long id) {
        productRepository.deleteById(id);
    }

}
