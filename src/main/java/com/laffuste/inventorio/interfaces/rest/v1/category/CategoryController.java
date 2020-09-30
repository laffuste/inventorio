package com.laffuste.inventorio.interfaces.rest.v1.category;

import com.google.common.base.Stopwatch;
import com.laffuste.inventorio.domain.category.Category;
import com.laffuste.inventorio.domain.category.CategoryManagementService;
import com.laffuste.inventorio.infrastructure.exception.ModelNotFoundException;
import com.laffuste.inventorio.interfaces.rest.v1.category.dto.CategoryDto;
import com.laffuste.inventorio.interfaces.rest.v1.category.dto.CategoryMapper;
import com.laffuste.inventorio.interfaces.rest.v1.category.dto.CreateCategoryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "categories", description = "view and manage product categories")
@RestController
@RequestMapping(path = "v1/categories", produces = {"application/json"})
@AllArgsConstructor
@Slf4j
public class CategoryController {

    private static final CategoryMapper categoryMapper = CategoryMapper.INSTANCE;
    private final CategoryManagementService categoryService;


    @Operation(summary = "Find all categories", description = "Retrieves all existing categories in the system, with their direct parent category.")
    @GetMapping
    public List<CategoryDto> getAllCategories() {
        Stopwatch watch = Stopwatch.createStarted();
        List<Category> categories = categoryService.findAll();
        List<CategoryDto> dtos = categoryMapper.toDtos(categories);
        log.info("Returning {} categories in {}", dtos.size(), watch);
        return dtos;
    }


    @Operation(summary = "Create a new category", description = "Creates a category or subcategory.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category created"),
            @ApiResponse(responseCode = "404", description = "Parent Category doesn't exist")
    })
    @PostMapping
    public CategoryDto createCategory(@RequestBody @Valid CreateCategoryDto createCategoryDto) throws ModelNotFoundException {
        log.warn("HI");
        log.warn(createCategoryDto.toString());
        Stopwatch watch = Stopwatch.createStarted();
        Category category = categoryMapper.toModel(createCategoryDto);
        Category newCategory = categoryService.create(category);
        log.info("Category {} created in {}: {}", newCategory.getName(), watch, newCategory);
        return categoryMapper.toDto(newCategory);
    }


    @Operation(summary = "Find a category", description = "Retrieves a category by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category returned"),
            @ApiResponse(responseCode = "404", description = "Category doesn't exist")
    })
    @GetMapping("/{id}")
    public CategoryDto getCategory(@PathVariable long id) throws ModelNotFoundException {
        Category category = categoryService.findById(id);
        return categoryMapper.toDto(category);
    }


    @Operation(summary = "Update a category", description = "Update a category by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated"),
            @ApiResponse(responseCode = "404", description = "Category doesn't exist")
    })
    @PutMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable long id, @RequestBody @Valid CreateCategoryDto createCategoryDto) throws ModelNotFoundException {
        Category category = categoryMapper.toModel(createCategoryDto);
        Category updatedCategory = categoryService.update(category.withId(id));
        return categoryMapper.toDto(updatedCategory);
    }


    @Operation(summary = "Delete a category", description = "Deletes a category by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted"),
            @ApiResponse(responseCode = "404", description = "Category doesn't exist")
    })
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable long id) throws ModelNotFoundException {
        categoryService.delete(id);
    }

}
