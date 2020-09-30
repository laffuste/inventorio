package com.laffuste.inventorio.domain.category;

import com.laffuste.inventorio.domain.product.Product;
import com.laffuste.inventorio.domain.product.ProductService;
import com.laffuste.inventorio.infrastructure.exception.BadRequestException;
import com.laffuste.inventorio.infrastructure.exception.ModelNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class CategoryManagementServiceTest {

    @InjectMocks
    private CategoryManagementService categoryManagementService;

    @Mock
    private CategoryService categoryService;
    @Mock
    private ProductService productService;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    void findAll() {
        // given
        List<Category> categories = asList(
                Category.builder().id(1L).name("cat a").build(),
                Category.builder().id(2L).name("cat b").build()
        );
        when(categoryService.findAll()).thenReturn(categories);

        // when
        List<Category> ret = categoryManagementService.findAll();

        // then
        assertThat(ret)
                .isNotNull()
                .hasSize(2)
                .extracting("id", "name")
                .containsExactly(
                        tuple(1L, "cat a"),
                        tuple(2L, "cat b")
                );
    }

    @Test
    void findAll_whenEmpty() {
        // given
        when(categoryService.findAll()).thenReturn(emptyList());

        // when
        List<Category> categories = categoryManagementService.findAll();

        // then
        assertThat(categories)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void findById() throws ModelNotFoundException {
        // given
        when(categoryService.findById(2L)).thenReturn(Optional.of(
                Category.builder()
                        .id(2L)
                        .name("cat a")
                        .build())
        );

        // when
        Category category = categoryManagementService.findById(2L);

        // then
        assertThat(category)
                .isNotNull()
                .extracting("id", "name")
                .containsExactly(2L, "cat a");
    }

    @Test
    public void findById_whenNotFound() {
        // given
        when(categoryService.findById(2L)).thenReturn(Optional.empty());

        // when
        Throwable t = catchThrowable(() -> categoryManagementService.findById(2L));

        // then
        assertThat(t)
                .isNotNull()
                .isInstanceOf(ModelNotFoundException.class)
                .hasMessage("Category with id 2 not found");
    }

    @Test
    public void create() throws ModelNotFoundException {
        // given
        mockCreateEntity(11L);

        Category category = Category.builder()
                .name("cat a")
                .build();

        // when
        Category newCategory = categoryManagementService.create(category);

        // then
        assertThat(newCategory)
                .isNotNull()
                .extracting("id", "name", "parentId")
                .containsExactly(11L, "cat a", null);
    }

    @Test
    public void create_withParentId() throws ModelNotFoundException {
        // given
        mockCreateEntity(11L);
        when(categoryService.exists(2L)).thenReturn(true);

        Category category = Category.builder()
                .name("cat a")
                .parentId(2L)
                .build();

        // when
        Category newCategory = categoryManagementService.create(category);

        // then
        assertThat(newCategory)
                .isNotNull()
                .extracting("id", "name", "parentId")
                .containsExactly(11L, "cat a", 2L);
    }

    @Test
    public void create_withParentId_whenNotExists_expectError() {
        mockCreateEntity(11L);
        when(categoryService.exists(2L)).thenReturn(false);
        Category category = Category.builder()
                .name("cat a")
                .parentId(2L)
                .build();

        // when
        Throwable t = catchThrowable(() -> categoryManagementService.create(category));

        // then
        assertThat(t)
                .isNotNull()
                .isInstanceOf(ModelNotFoundException.class)
                .hasMessage("Category with id 2 not found");
    }

    @Test
    public void create_whenIdSupplied_expectError() {
        // given
        Category category = Category.builder()
                .id(1L)
                .build();

        // when
        Throwable t = catchThrowable(() -> categoryManagementService.create(category));

        // then
        assertThat(t)
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Category id must be null when creating categories");
    }


    @Test
    public void update() throws ModelNotFoundException {
        // given
        Category category = Category.builder()
                .id(1L)
                .name("cat a")
                .parentId(2L)
                .build();
        mockUpdateEntity(1L);
        when(categoryService.exists(1L)).thenReturn(true);
        when(categoryService.exists(2L)).thenReturn(true);

        // when
        Category updatedCategory = categoryManagementService.update(category);

        // then
        assertThat(updatedCategory)
                .isNotNull()
                .extracting("id", "name", "parentId")
                .containsExactly(1L, "cat a", 2L);
    }

    @Test
    public void update_whenCategoryIdNotFound_expectError() {
        // given
        Category category = Category.builder()
                .id(1L)
                .name("cat a")
                .parentId(2L)
                .build();
        when(categoryService.exists(1L)).thenReturn(false);
        when(categoryService.exists(2L)).thenReturn(true);

        // when
        Throwable t = catchThrowable(() -> categoryManagementService.update(category));

        // then
        assertThat(t)
                .isNotNull()
                .isInstanceOf(ModelNotFoundException.class)
                .hasMessage("Category with id 1 not found");
    }

    @Test
    public void update_whenParentCategoryIdNotFound_expectError() {
        // given
        Category category = Category.builder()
                .id(1L)
                .name("cat a")
                .parentId(2L)
                .build();
        when(categoryService.exists(1L)).thenReturn(true);
        when(categoryService.exists(2L)).thenReturn(false);

        // when
        Throwable t = catchThrowable(() -> categoryManagementService.update(category));

        // then
        assertThat(t)
                .isNotNull()
                .isInstanceOf(ModelNotFoundException.class)
                .hasMessage("Category with id 2 not found");
    }

    @Test
    public void update_whenIdNull_expectError() {
        // given
        Category category = Category.builder()
                .id(null)
                .build();

        // when
        Throwable t = catchThrowable(() -> categoryManagementService.update(category));

        // then
        assertThat(t)
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Category id must not be null when updating categories");
    }

    @Test
    public void update_whenParentItself_expectError() {
        // given
        Category category = Category.builder()
                .id(1L)
                .parentId(1L)
                .build();

        // when
        Throwable t = catchThrowable(() -> categoryManagementService.update(category));

        // then
        assertThat(t)
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Parent category id cannot be itself (ID: 1)");
    }

    @Test
    public void delete() throws ModelNotFoundException {
        // given
        when(categoryService.exists(1L)).thenReturn(true);
        when(categoryService.findChildren(1L)).thenReturn(emptyList());
        when(productService.findByCategoryId(1L)).thenReturn(emptyList());

        // when
        categoryManagementService.delete(1L);

        // then
        verify(categoryService).delete(1L);
    }

    @Test
    public void delete_whenNotExists_expectError() {
        // given
        when(categoryService.exists(1L)).thenReturn(false);

        // when
        Throwable t = catchThrowable(() -> categoryManagementService.delete(1L));

        // then
        assertThat(t)
                .isNotNull()
                .isInstanceOf(ModelNotFoundException.class)
                .hasMessage("Category with id 1 not found");
    }

    @Test
    public void delete_whenHasSubcategories_expectError() {
        // given
        when(categoryService.exists(1L)).thenReturn(true);
        when(categoryService.findChildren(1L)).thenReturn(singletonList(
                Category.builder()
                        .id(2L)
                        .name("child cat")
                        .parentId(1L)
                        .build()
        ));

        // when
        Throwable t = catchThrowable(() -> categoryManagementService.delete(1L));

        // then
        assertThat(t)
                .isNotNull()
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Cannot delete category 1, it still has 1 subcategories: child cat (id: 2)");
    }

    @Test
    public void delete_whenHasProducts_expectError() {
        // given
        when(categoryService.exists(1L)).thenReturn(true);
        when(categoryService.findChildren(1L)).thenReturn(emptyList());
        when(productService.findByCategoryId(1L)).thenReturn(singletonList(
                Product.builder()
                        .id(3L)
                        .name("product from cat")
                        .categoryId(1L)
                        .build()
        ));

        // when
        Throwable t = catchThrowable(() -> categoryManagementService.delete(1L));

        // then
        assertThat(t)
                .isNotNull()
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Cannot delete category 1, it still has 1 products associated: product from cat (id: 3)");
    }
    
    
    private void mockCreateEntity(long newId) {
        when(categoryService.create(any(Category.class))).thenAnswer((mock) -> {
            Category cat = mock.getArgument(0);
            return cat.withId(newId);
        });
    }

    private void mockUpdateEntity(long newId) {
        when(categoryService.update(any(Category.class))).thenAnswer((mock) -> {
            Category cat = mock.getArgument(0);
            return cat.withId(newId);
        });
    }


}