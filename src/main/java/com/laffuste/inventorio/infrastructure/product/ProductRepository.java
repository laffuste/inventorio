package com.laffuste.inventorio.infrastructure.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, QuerydslPredicateExecutor<ProductEntity>, ProductRepositoryCustom {

    Optional<ProductEntity> findById(Long id);

    List<ProductEntity> findAllByCategoryId(long categoryId);

}
