package com.laffuste.inventorio.interfaces.rest.v1.category.dto;

import com.laffuste.inventorio.domain.category.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDto toDto(Category category);

    List<CategoryDto> toDtos(List<Category> category);

    Category toModel(CategoryDto category);

    Category toModel(CreateCategoryDto createCategoryDto);

}
