package com.laffuste.inventorio.infrastructure.product;

import com.laffuste.inventorio.domain.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductEntityMapper {

    ProductEntityMapper MAPPER = Mappers.getMapper(ProductEntityMapper.class);

    Product toModel(ProductEntity productEntity);

    List<Product> toModels(List<ProductEntity> productEntities);

    ProductEntity toEntity(Product product);

}
