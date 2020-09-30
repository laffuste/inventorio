package com.laffuste.inventorio.infrastructure.category;

import com.laffuste.inventorio.domain.category.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CategoryEntityMapper {

    CategoryEntityMapper MAPPER = Mappers.getMapper(CategoryEntityMapper.class);

    Category toModel(CategoryEntity category);

    List<Category> toModels(List<CategoryEntity> categories);

    CategoryEntity toEntity(Category category);

}
