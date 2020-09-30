package com.laffuste.inventorio.infrastructure.product;

import com.laffuste.inventorio.domain.product.Product;
import com.laffuste.inventorio.infrastructure.category.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;


class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryServiceImpl categoryServiceImpl;

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
        when(productRepository.search(eq(criteria), eq(PageRequest.of(0, 10)))).thenReturn(new PageImpl<>(singletonList(ProductEntity.builder().categoryId(1L).name("prod a").build())));

        // when
        Page<Product> productPage = productServiceImpl.search(criteria, 0, 10);

        // then
        assertThat(productPage)
                .isNotNull()
                .hasSize(1)
                .extracting("categoryId", "name")
                .containsExactly(tuple(1L, "prod a"));
    }

    @Test
    public void findById() {
        // given
        when(productRepository.findById(1L)).thenReturn(Optional.of(
                ProductEntity.builder()
                        .id(1L)
                        .name("prod a")
                        .categoryId(50L)
                        .build()
        ));

        // when
        Optional<Product> product = productServiceImpl.findById(1L);

        // then
        assertThat(product)
                .isNotNull()
                .isPresent()
                .get()
                .extracting("id", "name", "categoryId")
                .containsExactly(1L, "prod a", 50L);
    }

    @Test
    public void findByCategoryId() {
        // given
        when(productRepository.findAllByCategoryId(1L)).thenReturn(singletonList(
                ProductEntity.builder()
                        .id(1L)
                        .name("prod a")
                        .categoryId(50L)
                        .build()
        ));

        // when
        List<Product> products = productServiceImpl.findByCategoryId(1L);

        // then
        assertThat(products)
                .isNotNull()
                .hasSize(1)
                .extracting("id", "name", "categoryId")
                .containsExactly(tuple(1L, "prod a", 50L));
    }

    @Test
    public void findByCategoryId_whenNone() {
        // given
        when(productRepository.findAllByCategoryId(1L)).thenReturn(emptyList());

        // when
        List<Product> products = productServiceImpl.findByCategoryId(1L);

        // then
        assertThat(products)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void create() {
        // given
        mockSaveEntity(2L);
        Product product = Product.builder()
                .name("product")
                .categoryId(50L)
                .quantity(10)
                .build();
        when(categoryServiceImpl.exists(50L)).thenReturn(true);

        // when
        Product newProduct = productServiceImpl.create(product);

        // then
        assertThat(newProduct)
                .isNotNull()
                .extracting("id", "name", "categoryId")
                .containsExactly(2L, "product", 50L);
    }

    @Test
    public void update() {
        // given
        when(productRepository.existsById(1L)).thenReturn(true);
        when(categoryServiceImpl.exists(50L)).thenReturn(true);
        mockSaveEntity(1L);
        Product product = Product.builder()
                .id(1L)
                .name("new prod")
                .categoryId(50L)
                .build();

        // when
        Product savedProduct = productServiceImpl.update(product);

        // then
        assertThat(savedProduct)
                .isNotNull()
                .extracting("id", "name", "categoryId")
                .containsExactly(1L, "new prod", 50L);
    }

    @Test
    public void delete() {
        // given
        when(productRepository.existsById(1L)).thenReturn(true);

        // when
        productServiceImpl.delete(1L);

        // then
        verify(productRepository).deleteById(1L);
    }

    /* HELPERS */

    private void mockSaveEntity(long newId) {
        when(productRepository.save(any(ProductEntity.class))).thenAnswer((mock) -> {
            ProductEntity entity = mock.getArgument(0);
            entity.setId(newId);
            return entity;
        });
    }

}