package com.laffuste.inventorio.domain.category;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CategoryService {

    List<Category> findAll();

    Optional<Category> findById(long id);

    Category create(Category category);

    Category update(Category category);

    void delete(long id);

    boolean exists(long id);

    List<Category> findChildren(long id);

}
