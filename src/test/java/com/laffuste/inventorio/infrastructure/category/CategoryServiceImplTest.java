package com.laffuste.inventorio.infrastructure.category;

import com.laffuste.inventorio.domain.category.Category;
import com.laffuste.inventorio.infrastructure.product.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductServiceImpl productServiceImpl;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    void findAll() {
        // given
        List<CategoryEntity> categoriesDb = asList(
                CategoryEntity.builder().id(1L).name("cat a").build(),
                CategoryEntity.builder().id(2L).name("cat b").build()
        );
        when(categoryRepository.findAll()).thenReturn(categoriesDb);

        // when
        List<Category> categories = categoryServiceImpl.findAll();

        // then
        assertThat(categories)
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
        when(categoryRepository.findAll()).thenReturn(emptyList());

        // when
        List<Category> categories = categoryServiceImpl.findAll();

        // then
        assertThat(categories)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void findById() {
        // given
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(
                CategoryEntity.builder()
                        .id(2L)
                        .name("cat a")
                        .build())
        );

        // when
        Optional<Category> category = categoryServiceImpl.findById(2L);

        // then
        assertThat(category)
                .isNotNull()
                .isPresent()
                .get()
                .extracting("id", "name")
                .containsExactly(2L, "cat a");
    }

    @Test
    public void create() {
        // given
        mockSavEntity(11L);

        Category category = Category.builder()
                .name("cat a")
                .build();

        // when
        Category newCategory = categoryServiceImpl.create(category);

        // then
        assertThat(newCategory)
                .isNotNull()
                .extracting("id", "name", "parentId")
                .containsExactly(11L, "cat a", null);
    }

    @Test
    public void create_withParentId() {
        // given
        mockSavEntity(11L);
        when(categoryRepository.existsById(2L)).thenReturn(true);

        Category category = Category.builder()
                .name("cat a")
                .parentId(2L)
                .build();

        // when
        Category newCategory = categoryServiceImpl.create(category);

        // then
        assertThat(newCategory)
                .isNotNull()
                .extracting("id", "name", "parentId")
                .containsExactly(11L, "cat a", 2L);
    }

    @Test
    public void update() {
        // given
        Category category = Category.builder()
                .id(1L)
                .name("cat a")
                .parentId(2L)
                .build();
        mockSavEntity(1L);
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(categoryRepository.existsById(2L)).thenReturn(true);

        // when
        Category updatedCategory = categoryServiceImpl.update(category);

        // then
        assertThat(updatedCategory)
                .isNotNull()
                .extracting("id", "name", "parentId")
                .containsExactly(1L, "cat a", 2L);
    }

    @Test
    public void delete() {
        // given
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(categoryRepository.findAllByParentId(1L)).thenReturn(emptyList());
        when(productServiceImpl.findByCategoryId(1L)).thenReturn(emptyList());

        // when
        categoryServiceImpl.delete(1L);

        // then
        verify(categoryRepository).deleteById(1L);
    }


    /* HELPERS */

    private void mockSavEntity(long newId) {
        when(categoryRepository.save(any(CategoryEntity.class))).thenAnswer((mock) -> {
            CategoryEntity entity = mock.getArgument(0);
            entity.setId(newId);
            return entity;
        });
    }

}