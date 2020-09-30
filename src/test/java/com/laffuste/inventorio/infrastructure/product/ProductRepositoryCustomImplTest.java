package com.laffuste.inventorio.infrastructure.product;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.sql.DataSource;

import static com.ninja_squad.dbsetup.Operations.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

//@ExtendWith(SpringExtension.class)
@DataJpaTest
class ProductRepositoryCustomImplTest {

    @Autowired
    private DataSource dataSource;
    private static final DbSetupTracker dbSetupTracker = new DbSetupTracker();

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void prepare() {
        Operation operation =
                sequenceOf(
                        deleteAllFrom("product"),
                        insertInto("product")
                                .columns("name", "category_id", "quantity")

                                .values("adidas superstar", 1, 0) // shoes
                                .values("superdry shirt", 1, 10)
                                .values("levi's jeans", 1, 20)

                                .values("century egg", 2, 0)
                                .values("salty fish", 2, 30)

                                .build());

        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    void search_whenNoCriteria_returnAll() {
        // given
        dbSetupTracker.skipNextLaunch();
        SearchProductCriteria criteria = SearchProductCriteria.builder()
                .build();

        // when
        Page<ProductEntity> page = productRepository.search(criteria, Pageable.unpaged());

        // then
        assertThat(page)
                .isNotNull()
                .hasSize(5)
                .extracting("name")
                .containsExactly("adidas superstar", "superdry shirt", "levi's jeans", "century egg", "salty fish");
    }

    @Test
    void search_whenNoMatch_returnEmpty() {
        // given
        dbSetupTracker.skipNextLaunch();
        SearchProductCriteria criteria = SearchProductCriteria.builder()
                .name("non existing product")
                .build();

        // when
        Page<ProductEntity> page = productRepository.search(criteria, Pageable.unpaged());

        // then
        assertThat(page)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void search_whenPaging() {
        // given
        dbSetupTracker.skipNextLaunch();
        SearchProductCriteria criteria = SearchProductCriteria.builder()
                .build();
        PageRequest pageRequest = PageRequest.of(1, 2);

        // when
        Page<ProductEntity> page = productRepository.search(criteria, pageRequest);

        // then
        assertThat(page)
                .isNotNull()
                .hasSize(2)
                .extracting("name")
                .containsExactly("levi's jeans", "century egg");
    }

    @Test
    void search_whenFilteringByName() {
        // given
        dbSetupTracker.skipNextLaunch();
        SearchProductCriteria criteria = SearchProductCriteria.builder()
                .name("sh")
                .build();

        // when
        Page<ProductEntity> page = productRepository.search(criteria, Pageable.unpaged());

        // then
        assertThat(page)
                .isNotNull()
                .hasSize(2)
                .extracting("name")
                .containsExactly("superdry shirt", "salty fish");
    }

    @Test
    void search_whenFilteringByCategoryName() {
        // given
        dbSetupTracker.skipNextLaunch();
        SearchProductCriteria criteria = SearchProductCriteria.builder()
                .categoryId(1L)
                .build();

        // when
        Page<ProductEntity> page = productRepository.search(criteria, Pageable.unpaged());

        // then
        assertThat(page)
                .isNotNull()
                .hasSize(3)
                .extracting("name")
                .containsExactly("adidas superstar", "superdry shirt", "levi's jeans");
    }

    @Test
    void search_whenFilteringByAvailable() {
        // given
        dbSetupTracker.skipNextLaunch();
        SearchProductCriteria criteria = SearchProductCriteria.builder()
                .isAvailable(true)
                .build();

        // when
        Page<ProductEntity> page = productRepository.search(criteria, Pageable.unpaged());

        // then
        assertThat(page)
                .isNotNull()
                .hasSize(3)
                .extracting("name", "quantity")
                .containsExactly(
                        tuple("superdry shirt", 10),
                        tuple("levi's jeans", 20),
                        tuple("salty fish", 30)
                );
    }

    @Test
    void search_whenFilteringByNotAvailable() {
        // given
        dbSetupTracker.skipNextLaunch();
        SearchProductCriteria criteria = SearchProductCriteria.builder()
                .isAvailable(false)
                .build();

        // when
        Page<ProductEntity> page = productRepository.search(criteria, Pageable.unpaged());

        // then
        assertThat(page)
                .isNotNull()
                .hasSize(2)
                .extracting("name", "quantity")
                .containsExactly(
                        tuple("adidas superstar", 0),
                        tuple("century egg", 0)
                );
    }

    @Test
    void search_whenOrderingByName() {
        // given
        dbSetupTracker.skipNextLaunch();
        SearchProductCriteria criteria = SearchProductCriteria.builder()
                .build();
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));

        // when
        Page<ProductEntity> page = productRepository.search(criteria, pageRequest);

        // then
        assertThat(page)
                .isNotNull()
                .hasSize(5)
                .extracting("name")
                .containsExactly("adidas superstar", "century egg", "levi's jeans", "salty fish", "superdry shirt");
    }


    // TODO test min quantiuty

}