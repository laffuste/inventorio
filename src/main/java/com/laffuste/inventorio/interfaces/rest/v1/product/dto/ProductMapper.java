package com.laffuste.inventorio.interfaces.rest.v1.product.dto;

import com.laffuste.inventorio.domain.product.Product;
import com.laffuste.inventorio.infrastructure.product.SearchProductCriteria;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDto toDto(Product product);

    List<ProductDto> toDtos(List<Product> product);

    Product toModel(ProductDto productDto);

    List<Product> toModels(List<ProductDto> productDto);

    Product toModel(CreateProductDto createProductDto);

    SearchProductCriteria toCriteria(SearchProductRequest searchProductRequest);

}
