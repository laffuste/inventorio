package com.laffuste.inventorio.infrastructure.category;


import com.laffuste.inventorio.domain.category.Category;
import com.laffuste.inventorio.domain.category.CategoryService;
import com.laffuste.inventorio.infrastructure.product.ProductServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private static final CategoryEntityMapper categoryEntityMapper = CategoryEntityMapper.MAPPER;

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<Category> findAll() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        return categoryEntityMapper.toModels(categories);
    }

    @Transactional(readOnly = true)
    public Optional<Category> findById(long id) {
        return categoryRepository.findById(id).map(categoryEntityMapper::toModel);
    }

    public Category create(Category category) {
        CategoryEntity categoryEntity = categoryEntityMapper.toEntity(category);
        CategoryEntity savedCategory = categoryRepository.save(categoryEntity);
        return categoryEntityMapper.toModel(savedCategory);
    }

    public Category update(Category category) {
        CategoryEntity categoryEntity = categoryEntityMapper.toEntity(category);
        CategoryEntity updatedCategory = categoryRepository.save(categoryEntity);
        return categoryEntityMapper.toModel(updatedCategory);
    }

    public void delete(long id) {
        categoryRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean exists(long id) {
        return categoryRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public List<Category> findChildren(long id) {
        List<CategoryEntity> entities = categoryRepository.findAllByParentId(id);
        return categoryEntityMapper.toModels(entities);
    }

}
