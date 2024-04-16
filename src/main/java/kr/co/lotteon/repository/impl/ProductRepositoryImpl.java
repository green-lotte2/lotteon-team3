package kr.co.lotteon.repository.impl;


import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.product.AdminPageRequestDTO;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.product.QProduct;
import kr.co.lotteon.repository.custom.ProductRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QProduct qProduct = QProduct.product;

    // 관리자 - 상품 목록 기본 조회
    @Override
    public Page<Product> adminSelectProducts(AdminPageRequestDTO adminPageRequestDTO, Pageable pageable){
        log.info("상품 목록 기본 조회 Impl 1 : " + adminPageRequestDTO);
        QueryResults<Product> results = jpaQueryFactory
                .select(qProduct)
                .from(qProduct)
                .orderBy(qProduct.prodNo.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        long total = results.getTotal();
        log.info("상품 목록 기본 조회 Impl 2 : " + total);
        List<Product> productList = results.getResults();
        log.info("상품 목록 기본 조회 Impl 3 : " + productList);
        return new PageImpl<>(productList, pageable, total);
    }
    // 관리자 - 상품 목록 검색 조회
    @Override
    public Page<Product> adminSearchProducts(AdminPageRequestDTO adminPageRequestDTO, Pageable pageable){
        return null;
    }
}
