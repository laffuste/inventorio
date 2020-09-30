package com.laffuste.inventorio.domain.category;

import com.laffuste.inventorio.domain.product.Product;
import com.laffuste.inventorio.domain.product.ProductService;
import com.laffuste.inventorio.infrastructure.exception.BadRequestException;
import com.laffuste.inventorio.infrastructure.exception.ModelNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

@Service
@AllArgsConstructor
public class CategoryManagementService {

    private final CategoryService categoryService;
    private final ProductService productService;

    public List<Category> findAll() {
        return categoryService.findAll();
    }

    public Category findById(long id) throws ModelNotFoundException {
        return categoryService.findById(id).orElseThrow(() -> new ModelNotFoundException(Category.class, id));
    }

    public Category create(Category category) throws ModelNotFoundException {
        checkArgument(category.getId() == null, "Category id must be null when creating categories");
        if (category.getParentId() != null && !categoryService.exists(category.getParentId())) {
            // parent category doesn't exist
            throw new ModelNotFoundException(Category.class, category.getParentId());
        }

        return categoryService.create(category);
    }

    public Category update(Category category) throws ModelNotFoundException {
        checkArgument(category.getId() != null, "Category id must not be null when updating categories");
        checkArgument(!category.getId().equals(category.getParentId()), "Parent category id cannot be itself (ID: %s)", category.getId());
        if (!categoryService.exists(category.getId())) {
            throw new ModelNotFoundException(Category.class, category.getId());
        }
        if (!categoryService.exists(category.getParentId())) {
            throw new ModelNotFoundException(Category.class, category.getParentId());
        }

        return categoryService.update(category);
    }

    public void delete(long id) throws ModelNotFoundException {
        if (!categoryService.exists(id)) {
            throw new ModelNotFoundException(Category.class, id);
        }
        List<Category> children = categoryService.findChildren(id);
        if (!children.isEmpty()) {
            String childrenDesc = children.stream()
                    .map(c -> format("%s (id: %s)", c.getName(), c.getId()))
                    .collect(joining(", "));
            String msg = format("Cannot delete category %s, it still has %d subcategories: %s", id, children.size(), childrenDesc);
            throw new BadRequestException(msg);
        }

        List<Product> productsInCategory = productService.findByCategoryId(id);
        if (!productsInCategory.isEmpty()) {
            String productsDesc = productsInCategory.stream()
                    .map(c -> format("%s (id: %s)", c.getName(), c.getId()))
                    .collect(joining(", "));
            String msg = format("Cannot delete category %s, it still has %d products associated: %s", id, productsInCategory.size(), productsDesc);
            throw new BadRequestException(msg);
        }

        categoryService.delete(id);
    }

}
