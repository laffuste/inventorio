package com.laffuste.inventorio.infrastructure.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>, QuerydslPredicateExecutor<CategoryEntity> {

    List<CategoryEntity> findAllByParentId(long parentId);

}
