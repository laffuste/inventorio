package com.laffuste.inventorio.domain.product;

import com.laffuste.inventorio.domain.category.CategoryService;
import com.laffuste.inventorio.infrastructure.exception.ModelNotFoundException;
import com.laffuste.inventorio.infrastructure.product.SearchProductCriteria;
import com.laffuste.inventorio.interfaces.config.InventorioConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class ProductManagementServiceTest {

    @InjectMocks
    private ProductManagementService productManagementService;

    @Mock
    private ProductService productService;
    @Mock
    private CategoryService categoryService;
    @Mock
    private InventorioConfig inventorioConfig;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    public void search() {
        // given
        SearchProductCriteria criteria = SearchProductCriteria.builder()
                .name("a")
                .isAvailable(true)
                .categoryId(1L)
                .build();
        when(productService.search(eq(criteria), eq(0), eq(99))).thenReturn(new PageImpl<>(singletonList(Product.builder().categoryId(1L).name("prod a").build())));
        when(inventorioConfig.getProductPaging()).thenReturn(99);

        // when
        Page<Product> productPage = productManagementService.search(criteria, 0);

        // then
        assertThat(productPage)
                .isNotNull()
                .hasSize(1)
                .extracting("categoryId", "name")
                .containsExactly(tuple(1L, "prod a"));
    }

    @Test
    public void findById() throws ModelNotFoundException {
        // given
        when(productService.findById(1L)).thenReturn(Optional.of(
                Product.builder()
                        .id(1L)
                        .name("prod a")
                        .categoryId(50L)
                        .build()
        ));

        // when
        Product product = productManagementService.findById(1L);

        // then
        assertThat(product)
                .isNotNull()
                .extracting("id", "name", "categoryId")
                .containsExactly(1L, "prod a", 50L);
    }

    @Test
    public void findById_whenNotFound_expectError() {
        // given
        when(productService.findById(1L)).thenReturn(Optional.empty());

        // when
        Throwable t = catchThrowable(() -> productManagementService.findById(1L));

        // then
        assertThat(t)
                .isNotNull()
                .isInstanceOf(ModelNotFoundException.class)
                .hasMessage("Product with id 1 not found");
    }

    @Test
    public void create() throws ModelNotFoundException {
        // given
        mockSaveEntity(2L);
        Product product = Product.builder()
                .name("product")
                .categoryId(50L)
                .quantity(10)
                .build();
        when(categoryService.exists(50L)).thenReturn(true);

        // when
        Product newProduct = productManagementService.create(product);

        // then
        assertThat(newProduct)
                .isNotNull()
                .extracting("id", "name", "categoryId")
                .containsExactly(2L, "product", 50L);
    }

    @Test
    public void create_withId_expectError() {
        // given
        Product product = Product.builder()
                .id(1L)
                .build();

        // when
        Throwable t = catchThrowable(() -> productManagementService.create(product));

        // then
        assertThat(t)
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product id must be null when creating products");
    }

    @Test
    public void create_whenCategoryNotFound_expectError() {
        // given
        Product product = Product.builder()
                .name("new prod")
                .categoryId(2L)
                .build();
        when(categoryService.exists(2L)).thenReturn(false);

        // when
        Throwable t = catchThrowable(() -> productManagementService.create(product));

        // then
        assertThat(t)
                .isNotNull()
                .isInstanceOf(ModelNotFoundException.class)
                .hasMessage("Category with id 2 not found");
    }

    @Test
    public void update() throws ModelNotFoundException {
        // given
        when(productService.exists(1L)).thenReturn(true);
        when(categoryService.exists(50L)).thenReturn(true);
        mockUpdateEntity(1L);
        Product product = Product.builder()
                .id(1L)
                .name("new prod")
                .categoryId(50L)
                .build();

        // when
        Product savedProduct = productManagementService.update(product);

        // then
        assertThat(savedProduct)
                .isNotNull()
                .extracting("id", "name", "categoryId")
                .containsExactly(1L, "new prod", 50L);
    }

    @Test
    public void update_whenNoId_expectError() {
        // given
        Product product = Product.builder()
                .build();

        // when
        Throwable t = catchThrowable(() -> productManagementService.update(product));

        // then
        assertThat(t)
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product id must not be null when updating products");
    }

    @Test
    public void update_whenProductDoesntExist_expectError() {
        // given
        when(productService.exists(1L)).thenReturn(false);
        when(categoryService.exists(50L)).thenReturn(true);
        Product product = Product.builder()
                .id(1L)
                .name("new prod")
                .categoryId(2L)
                .build();

        // when
        Throwable t = catchThrowable(() -> productManagementService.update(product));

        // then
        assertThat(t)
                .isNotNull()
                .isInstanceOf(ModelNotFoundException.class)
                .hasMessage("Product with id 1 not found");
    }

    @Test
    public void update_whenCategoryDoesntExist_expectError() {
        // given
        when(productService.exists(1L)).thenReturn(true);
        when(categoryService.exists(50L)).thenReturn(false);
        Product product = Product.builder()
                .id(1L)
                .name("new prod")
                .categoryId(50L)
                .build();

        // when
        Throwable t = catchThrowable(() -> productManagementService.update(product));

        // then
        assertThat(t)
                .isNotNull()
                .isInstanceOf(ModelNotFoundException.class)
                .hasMessage("Category with id 50 not found");
    }


    @Test
    public void delete() throws ModelNotFoundException {
        // given
        when(productService.exists(1L)).thenReturn(true);

        // when
        productManagementService.delete(1L);

        // then
        verify(productService).delete(1L);
    }

    @Test
    public void delete_whenNotExist_expectError() {
        // given
        when(productService.exists(1L)).thenReturn(false);

        // when
        Throwable t = catchThrowable(() -> productManagementService.delete(1L));

        // then
        assertThat(t)
                .isNotNull()
                .isInstanceOf(ModelNotFoundException.class)
                .hasMessage("Product with id 1 not found");
    }


    /* HELPERS */

    private void mockSaveEntity(long newId) {
        when(productService.create(any(Product.class))).thenAnswer((mock) -> {
            Product product = mock.getArgument(0);
            return product.withId(newId);
        });
    }

    private void mockUpdateEntity(long newId) {
        when(productService.update(any(Product.class))).thenAnswer((mock) -> {
            Product product = mock.getArgument(0);
            return product.withId(newId);
        });
    }

}