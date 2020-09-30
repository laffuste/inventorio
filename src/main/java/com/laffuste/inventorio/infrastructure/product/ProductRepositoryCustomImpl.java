package com.laffuste.inventorio.infrastructure.product;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Repository
public class ProductRepositoryCustomImpl extends QuerydslRepositorySupport implements ProductRepositoryCustom {

    public ProductRepositoryCustomImpl() {
        super(ProductEntity.class);
    }

    /**
     * Dynamic search based on filters
     */
    @Override
    public Page<ProductEntity> search(SearchProductCriteria criteria, Pageable pageable) {
        JPQLQuery<ProductEntity> query = from(QProductEntity.productEntity);
        if (criteria.getCategoryId() != null) {
            query.where(QProductEntity.productEntity.categoryId.eq(criteria.getCategoryId()));
        }
        if (criteria.getIsAvailable() != null) {
            if (criteria.getIsAvailable()) {
                query.where(QProductEntity.productEntity.quantity.gt(0L));
            } else {
                query.where(QProductEntity.productEntity.quantity.eq(0));
            }
        }
        if (isNotBlank(criteria.getName())) {
            query.where(QProductEntity.productEntity.name.contains(criteria.getName()));
        }

        query = super.getQuerydsl().applyPagination(pageable, query);  // paginate + order
        QueryResults<ProductEntity> res = query.fetchResults();
        return new PageImpl<>(res.getResults(), pageable, res.getTotal());
    }

}