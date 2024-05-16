package kr.co.lotteon.repository.impl;


import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.admin.AdminProductPageRequestDTO;
import kr.co.lotteon.dto.product.PageRequestDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.dto.product.ProductReviewPageRequestDTO;
import kr.co.lotteon.dto.product.SearchPageRequestDTO;
import kr.co.lotteon.entity.member.QMember;
import kr.co.lotteon.entity.product.*;
import kr.co.lotteon.repository.custom.ProductRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final ModelMapper modelMapper;
    private final QProduct qProduct = QProduct.product;
    private final QOption qOption = QOption.option;
    private final QReview qReview = QReview.review;
    private final QMember qMember = QMember.member;
    private final QOrderItem qOrderItem=QOrderItem.orderItem;

    // 관리자 - 상품 목록 기본 조회
    @Override
    public Page<Product> adminSelectProducts(AdminProductPageRequestDTO adminProductPageRequestDTO, Pageable pageable) {
        log.info("상품 목록 기본 조회 Impl 1 : " + adminProductPageRequestDTO);
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
    public Page<Product> adminSearchProducts(AdminProductPageRequestDTO adminProductPageRequestDTO, Pageable pageable) {
        log.info("상품 목록 키워드 검색 impl 1 : " + adminProductPageRequestDTO.getKeyword());
        String type = adminProductPageRequestDTO.getType();
        String keyword = adminProductPageRequestDTO.getKeyword();

        BooleanExpression expression = null;

        // 검색 종류에 따른 where절 표현식 생성
        if (type.equals("prodName")) {
            expression = qProduct.prodName.contains(keyword);
            log.info("prodName 검색 : " + expression);

        } else if (type.equals("prodNo")) {
            // 입력된 키워드를 정수형으로 변환
            int prodNo = Integer.parseInt(keyword);
            expression = qProduct.prodNo.eq(prodNo);
            log.info("prodCode 검색 : " + expression);

        } else if (type.equals("cate1")) {
            int cate1 = Integer.parseInt(keyword);
            expression = qProduct.cate1.eq(cate1);
            log.info("cate1 검색 : " + expression);

        } else if (type.equals("company")) {
            expression = qProduct.company.contains(keyword);
            log.info("company 검색 : " + expression);

        } else if (type.equals("status")) {
            switch (keyword) {
                case "0" :
                    expression = qProduct.status.contains("새상품");
                    break;
                case "1" :
                    expression = qProduct.status.contains("품절");
                    break;
                case "2" :
                    expression = qProduct.status.contains("삭제된 상품");
                    break;
            }
            log.info("status 검색 : " + expression);

        } else if (type.equals("seller")) {
            expression = qProduct.seller.contains(keyword);
            log.info("seller 검색 : " + expression);
        }
        // DB 조회
        QueryResults<Product> results = jpaQueryFactory
                .select(qProduct)
                .from(qProduct)
                .where(expression)
                .orderBy(qProduct.prodNo.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        long total = results.getTotal();
        log.info("상품 목록 검색 조회 Impl 2 : " + total);

        // QueryResults<> -> List<>
        List<Product> productList = results.getResults();
        log.info("상품 목록 검색 조회 Impl 3 : " + productList);
        return new PageImpl<>(productList, pageable, total);
    }

    // 판매자  - 상품 목록 기본 조회
    public Page<Product> sellerSelectProducts(AdminProductPageRequestDTO adminProductPageRequestDTO, Pageable pageable, String sellerId) {

        log.info("상품 목록 기본 조회 Impl 1 : " + adminProductPageRequestDTO);
        QueryResults<Product> results = jpaQueryFactory
                .select(qProduct)
                .from(qProduct)
                .where(qProduct.seller.eq(sellerId))
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

    // 판매자  - 상품 목록 검색 조회
    public Page<Product> sellerSearchProducts(AdminProductPageRequestDTO adminProductPageRequestDTO, Pageable pageable, String sellerId) {
        log.info("상품 목록 키워드 검색 impl 1 : " + adminProductPageRequestDTO.getKeyword());
        String type = adminProductPageRequestDTO.getType();
        String keyword = adminProductPageRequestDTO.getKeyword();

        BooleanExpression expression = null;

        // 검색 종류에 따른 where절 표현식 생성
        if (type.equals("prodName")) {
            expression = qProduct.prodName.contains(keyword).and(qProduct.seller.eq(sellerId));
            log.info("prodName 검색 : " + expression);

        } else if (type.equals("prodNo")) {
            // 입력된 키워드를 정수형으로 변환
            int prodNo = Integer.parseInt(keyword);
            expression = qProduct.prodNo.eq(prodNo).and(qProduct.seller.eq(sellerId));
            log.info("prodNo 검색 : " + expression);

        } else if (type.equals("status")) {
            switch (keyword) {
                case "0" :
                    expression = qProduct.status.contains("새상품").and(qProduct.seller.eq(sellerId));
                    break;
                case "1" :
                    expression = qProduct.status.contains("품절").and(qProduct.seller.eq(sellerId));
                    break;
                case "2" :
                    expression = qProduct.status.contains("삭제된 상품").and(qProduct.seller.eq(sellerId));
                    break;
            }
            log.info("status 검색 : " + expression);

        } else if (type.equals("cate1")) {
            int cate1 = Integer.parseInt(keyword);
            expression = qProduct.cate1.eq(cate1).and(qProduct.seller.eq(sellerId));
            log.info("cate1 검색 : " + expression);

        } else if (type.equals("company")) {
            expression = qProduct.company.contains(keyword).and(qProduct.seller.eq(sellerId));
            log.info("company 검색 : " + expression);

        }

        // DB 조회
        QueryResults<Product> results = jpaQueryFactory
                .select(qProduct)
                .from(qProduct)
                .where(expression)
                .orderBy(qProduct.prodNo.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        long total = results.getTotal();
        log.info("상품 목록 검색 조회 Impl 2 : " + total);

        // QueryResults<> -> List<>
        List<Product> productList = results.getResults();
        log.info("상품 목록 검색 조회 Impl 3 : " + productList);
        return new PageImpl<>(productList, pageable, total);
    }

    // 기본 상품 리스트
    @Override
    public Page<Product> productList(PageRequestDTO pageRequestDTO, Pageable pageable) {
        log.info("productLsit impl" + pageRequestDTO);
        BooleanExpression predicate = qProduct.isNotNull();

        if (pageRequestDTO.getCate1() != 0) {
            predicate = predicate.and(qProduct.cate1.eq(pageRequestDTO.getCate1()));
        }
        if (pageRequestDTO.getCate2() != 0) {
            predicate = predicate.and(qProduct.cate2.eq(pageRequestDTO.getCate2()));
        }
        if (pageRequestDTO.getCate3() != 0) {
            predicate = predicate.and(qProduct.cate3.eq(pageRequestDTO.getCate3()));
        }

        // 정렬 방식을 지정하는 Order 객체 생성
        com.querydsl.core.types.Order order;
        if ("DESC".equals(pageRequestDTO.getHow())) {
            order = com.querydsl.core.types.Order.DESC;
        }else {
            order = com.querydsl.core.types.Order.ASC;
        }

        // 정렬 기준에 따라 쿼리 정렬 방식 변경
        OrderSpecifier<?> orderSpecifier;
        if (pageRequestDTO.getSort().equals("sold")) {
            orderSpecifier = new OrderSpecifier<>(order, qProduct.sold);
        } else if (pageRequestDTO.getSort().equals("price")) {
            orderSpecifier = new OrderSpecifier<>(order, qProduct.price);
        } else if (pageRequestDTO.getSort().equals("score")) {
            orderSpecifier = new OrderSpecifier<>(order, qProduct.score);
        }else if (pageRequestDTO.getSort().equals("review")) {
            orderSpecifier = new OrderSpecifier<>(order, qProduct.review);
        }else if (pageRequestDTO.getSort().equals("rdate")) {
            orderSpecifier = new OrderSpecifier<>(order, qProduct.rdate);
        } else {
            // 기본적으로 prodNo로 정렬
            orderSpecifier = qProduct.prodNo.desc();
        }

        QueryResults<Product> results = jpaQueryFactory.selectFrom(qProduct)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(predicate.and(qProduct.status.eq("새상품")))
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    // 판매자 - 상품 목록 All
    public List<Integer> selectProdNoForQna(String sellerId) {
        // SELECT ProdNo FROM product WHERE seller = ?;

        log.info("aaa : "+sellerId);
        List<Integer> prodNos = jpaQueryFactory
                .select(qProduct.prodNo)
                .from(qProduct)
                .where(qProduct.seller.eq(sellerId))
                .fetch();

        log.info("판매자 상품 번호 All 조회 Impl");

        return prodNos;
    }

    // ==== 메인 페이지 ====
    // 베스트 상품
    @Override
    public List<ProductDTO> bestProductMain() {
        // SELECT * FROM PRODUCT ORDER BY sold DESC LIMIT 5
        List<Product> products = jpaQueryFactory.selectFrom(qProduct)
                .where(qProduct.status.eq("새상품"))
                .orderBy(qProduct.sold.desc())
                .limit(5)
                .fetch();

        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> bestProductMain2() {
        // SELECT * FROM PRODUCT ORDER BY sold DESC LIMIT 5
        List<Product> products = jpaQueryFactory.selectFrom(qProduct)
                .where(qProduct.status.eq("새상품"))
                .orderBy(qProduct.sold.desc())
                .limit(8)
                .fetch();

        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    // 최신상품
    @Override
    public List<ProductDTO> recentProductMain() {
        // SELECT * FROM PRODUCT ORDER BY rdate DESC LIMIT 8
        List<Product> products = jpaQueryFactory.selectFrom(qProduct)
                .where(qProduct.status.eq("새상품"))
                .orderBy(qProduct.rdate.desc())
                .limit(8)
                .fetch();

        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    // 할인상품
    @Override
    public List<ProductDTO> discountProductMain() {
        // SELECT * FROM PRODUCT ORDER BY discount DESC LIMIT 8
        List<Product> products = jpaQueryFactory.selectFrom(qProduct)
                .where(qProduct.status.eq("새상품"))
                .orderBy(qProduct.discount.desc())
                .limit(8)
                .fetch();
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    // 히트상품
    @Override
    public List<ProductDTO> hitProductMain() {
        // SELECT * FROM PRODUCT ORDER BY discount DESC LIMIT 8
        List<Product> products = jpaQueryFactory.selectFrom(qProduct)
                .where(qProduct.status.eq("새상품"))
                .orderBy(qProduct.hit.desc())
                .limit(8)
                .fetch();

        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    // 추천 상품
    @Override
    public List<ProductDTO> recommendProductMain() {
        // SELECT * FROM PRODUCT ORDER BY score DESC LIMIT 8
        List<Product> products = jpaQueryFactory.selectFrom(qProduct)
                .where(qProduct.status.eq("새상품"))
                .orderBy(qProduct.score.desc())
                .limit(8)
                .fetch();
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    // =====================
    @Override
    public Page<Tuple> memberSelectReview(String uid, ProductReviewPageRequestDTO productReviewPageRequestDTO, Pageable pageable) {

        log.info("마이페이지 리뷰내역 목록 조회 Impl 1 : " + productReviewPageRequestDTO);
        QueryResults<Tuple> results = jpaQueryFactory
                .select(qReview, qProduct.prodName, qProduct.cate1, qProduct.cate2)
                .from(qReview)
                .where(qReview.uid.eq(uid))
                .join(qProduct).on(qReview.prodNo.eq(qProduct.prodNo))
                .orderBy(qReview.rdate.desc()) // rdate를 기준으로 내림차순으로 정렬)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Tuple> review = results.getResults();
        log.info("마이페이지 리뷰내역 목록 조회 Impl 2 : " + review);

        long total = results.getTotal();

        log.info("마이페이지 리뷰내역 목록 조회 Impl 3 : " + total);

        return new PageImpl<>(review, pageable, total);

    }

    @Override
    public Page<Tuple> selectProductReview(int prodNo, ProductReviewPageRequestDTO productReviewPageRequestDTO, Pageable pageable) {
        log.info("상품 리뷰내역 목록 조회 Impl 1 : " + productReviewPageRequestDTO);
        QueryResults<Tuple> results = jpaQueryFactory
                .select(qReview, qProduct.prodName, qMember.nick , qOrderItem.opNo )
                .from(qReview)
                .join(qProduct).on(qReview.prodNo.eq(qProduct.prodNo))
                .join(qMember).on(qReview.uid.eq(qMember.uid))
                .join(qOrderItem).on(qReview.ordItemno.eq(qOrderItem.ordItemno))
                .where(qReview.prodNo.eq(prodNo))
                .orderBy(qReview.rdate.desc()) // rdate를 기준으로 내림차순으로 정렬)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Tuple> review = results.getResults();
        log.info("상품 리뷰내역 목록 조회 Impl 2 : " + review);
        long total = results.getTotal();
        log.info("상품 리뷰내역 목록 조회 Impl 3 : " + total);

        return new PageImpl<>(review, pageable, total);

    }

    // 상품 - 전체옵션으로 조회
    @Override
    public Page<Tuple> searchProducts(SearchPageRequestDTO searchPageRequestDTO, Pageable pageable) {
        String searchKeyword = searchPageRequestDTO.getSearchKeyword();

        // 정렬 방식을 지정하는 Order 객체 생성
        com.querydsl.core.types.Order order;
        if ("DESC".equals(searchPageRequestDTO.getHow())) {
            order = com.querydsl.core.types.Order.DESC;
        } else {
            order = com.querydsl.core.types.Order.ASC;
        }

        // 정렬 기준에 따라 쿼리 정렬 방식 변경
        OrderSpecifier<?> orderSpecifier;

        if (searchPageRequestDTO.getSort().equals("sold")) {
            orderSpecifier = new OrderSpecifier<>(order, qProduct.sold);
        } else if (searchPageRequestDTO.getSort().equals("price")) {
            // 할인된 가격으로 정렬하도록 변경
            orderSpecifier = new OrderSpecifier<>(order, qProduct.price.subtract(qProduct.price.multiply(qProduct.discount.divide(100))));
        } else if (searchPageRequestDTO.getSort().equals("score")) {
            orderSpecifier = new OrderSpecifier<>(order, qProduct.score);
        } else if (searchPageRequestDTO.getSort().equals("review")) {
            orderSpecifier = new OrderSpecifier<>(order, qProduct.review);
        } else if (searchPageRequestDTO.getSort().equals("rdate")) {
            orderSpecifier = new OrderSpecifier<>(order, qProduct.rdate);
        } else {
            // 기본적으로 prodNo로 정렬
            orderSpecifier = qProduct.prodNo.desc();
        }

        BooleanExpression expression = qProduct.prodName.containsIgnoreCase(searchKeyword);

        QueryResults<Tuple> results = jpaQueryFactory
                .select(qProduct.prodNo, qProduct.prodName, qProduct.descript, qProduct.discount, qProduct.price, qProduct.seller, qProduct.delivery,
                        qProduct.thumb1, qProduct.sold, qProduct.score, qProduct.review, qProduct.rdate, qProduct.cate1, qProduct.cate2, qProduct.cate3)
                .from(qProduct)
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifier)
                .fetchResults();

        long total = results.getTotal();
        List<Tuple> content = results.getResults();

        return new PageImpl<>(content, pageable, total);
    }


    // 상품 - 상품이름으로 조회
    @Override
    public Page<Tuple> searchProductsProdName(SearchPageRequestDTO searchPageRequestDTO, Pageable pageable) {
        String searchKeyword = searchPageRequestDTO.getSearchKeyword();

        // 정렬 방식을 지정하는 Order 객체 생성
        com.querydsl.core.types.Order order;
        if ("DESC".equals(searchPageRequestDTO.getHow())) {
            order = com.querydsl.core.types.Order.DESC;
        }else {
            order = com.querydsl.core.types.Order.ASC;
        }

        // 정렬 기준에 따라 쿼리 정렬 방식 변경
        OrderSpecifier<?> orderSpecifier;

        if (searchPageRequestDTO.getSort().equals("sold")) {
            orderSpecifier = new OrderSpecifier<>(order, qProduct.sold);
        } else if (searchPageRequestDTO.getSort().equals("price")) {
            // 할인된 가격으로 정렬하도록 변경
            orderSpecifier = new OrderSpecifier<>(order, qProduct.price.subtract(qProduct.price.multiply(qProduct.discount.divide(100))));
        } else if (searchPageRequestDTO.getSort().equals("score")) {
            orderSpecifier = new OrderSpecifier<>(order, qProduct.score);
        }else if (searchPageRequestDTO.getSort().equals("review")) {
            orderSpecifier = new OrderSpecifier<>(order, qProduct.review);
        }else if (searchPageRequestDTO.getSort().equals("rdate")) {
            orderSpecifier = new OrderSpecifier<>(order, qProduct.rdate);
        } else {
            // 기본적으로 prodNo로 정렬
            orderSpecifier = qProduct.prodNo.desc();
        }

        BooleanExpression expression = qProduct.prodName.containsIgnoreCase(searchKeyword);

        QueryResults<Tuple> results = jpaQueryFactory
                .select(qProduct.prodNo, qProduct.prodName, qProduct.descript, qProduct.discount, qProduct.price, qProduct.seller, qProduct.delivery,
                        qProduct.thumb1, qProduct.sold, qProduct.score, qProduct.review, qProduct.rdate, qProduct.cate1, qProduct.cate2, qProduct.cate3)
                .from(qProduct)
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifier)
                .fetchResults();

        long total = results.getTotal();
        List<Tuple> content = results.getResults();

        return new PageImpl<>(content, pageable, total);
    }

    // 상품 - 상품설명으로 조회
    @Override
    public Page<Tuple> searchProductsDescript(SearchPageRequestDTO searchPageRequestDTO, Pageable pageable) {
        String searchKeyword = searchPageRequestDTO.getSearchKeyword();

        // 정렬 방식을 지정하는 Order 객체 생성
        com.querydsl.core.types.Order order;
        if ("DESC".equals(searchPageRequestDTO.getHow())) {
            order = com.querydsl.core.types.Order.DESC;
        }else {
            order = com.querydsl.core.types.Order.ASC;
        }

        // 정렬 기준에 따라 쿼리 정렬 방식 변경
        OrderSpecifier<?> orderSpecifier;
        if (searchPageRequestDTO.getSort().equals("sold")) {
            orderSpecifier = new OrderSpecifier<>(order, qProduct.sold);
        } else if (searchPageRequestDTO.getSort().equals("price")) {
            // 할인된 가격으로 정렬하도록 변경
            orderSpecifier = new OrderSpecifier<>(order, qProduct.price.subtract(qProduct.price.multiply(qProduct.discount.divide(100))));
        } else if (searchPageRequestDTO.getSort().equals("score")) {
            orderSpecifier = new OrderSpecifier<>(order, qProduct.score);
        }else if (searchPageRequestDTO.getSort().equals("review")) {
            orderSpecifier = new OrderSpecifier<>(order, qProduct.review);
        }else if (searchPageRequestDTO.getSort().equals("rdate")) {
            orderSpecifier = new OrderSpecifier<>(order, qProduct.rdate);
        } else {
            // 기본적으로 prodNo로 정렬
            orderSpecifier = qProduct.prodNo.desc();
        }

        BooleanExpression expression = qProduct.descript.containsIgnoreCase(searchKeyword);

        QueryResults<Tuple> results = jpaQueryFactory
                .select(qProduct.prodNo, qProduct.prodName, qProduct.descript, qProduct.discount, qProduct.price, qProduct.seller, qProduct.delivery,
                        qProduct.thumb1, qProduct.sold, qProduct.score, qProduct.review, qProduct.rdate, qProduct.cate1, qProduct.cate2, qProduct.cate3)
                .from(qProduct)
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifier)
                .fetchResults();

        long total = results.getTotal();
        List<Tuple> content = results.getResults();

        return new PageImpl<>(content, pageable, total);
    }

    // 상품 - 상품가격(할인된 가격임)으로 조회
    @Override
    public Page<Tuple> searchProductsPrice(SearchPageRequestDTO searchPageRequestDTO, Pageable pageable, int min, int max) {
        String searchKeyword = searchPageRequestDTO.getSearchKeyword();

        log.info("임플플..1" + searchKeyword);
        log.info("임플플..2" + searchPageRequestDTO.getSearchType());

        // 정렬 방식을 지정하는 Order 객체 생성
        com.querydsl.core.types.Order order;
        if ("DESC".equals(searchPageRequestDTO.getHow())) {
            order = com.querydsl.core.types.Order.DESC;
        }else {
            order = com.querydsl.core.types.Order.ASC;
        }

        // 정렬 기준에 따라 쿼리 정렬 방식 변경
        OrderSpecifier<?> orderSpecifier;
        if (searchPageRequestDTO.getSort().equals("sold")) {
            orderSpecifier = new OrderSpecifier<>(order, qProduct.sold);
        } else if (searchPageRequestDTO.getSort().equals("price")) {
            // 할인된 가격으로 정렬하도록 변경
            orderSpecifier = new OrderSpecifier<>(order, qProduct.price.subtract(qProduct.price.multiply(qProduct.discount.divide(100))));
        } else if (searchPageRequestDTO.getSort().equals("score")) {
            orderSpecifier = new OrderSpecifier<>(order, qProduct.score);
        }else if (searchPageRequestDTO.getSort().equals("review")) {
            orderSpecifier = new OrderSpecifier<>(order, qProduct.review);
        }else if (searchPageRequestDTO.getSort().equals("rdate")) {
            orderSpecifier = new OrderSpecifier<>(order, qProduct.rdate);
        } else {
            // 기본적으로 prodNo로 정렬
            orderSpecifier = qProduct.prodNo.desc();
        }

        BooleanExpression expression = null;

        if(searchPageRequestDTO.getSearchType().equals("name,price")){
            // 할인된 가격 범위를 기준으로 검색하는 표현식 생성
            expression = qProduct
                    .prodName.containsIgnoreCase(searchKeyword)
                    .and(qProduct.price.subtract(qProduct.price.multiply(qProduct.discount).divide(100)).between(min, max));
        }else{
            expression = qProduct
                    .descript.containsIgnoreCase(searchKeyword)
                    .and(qProduct.price.subtract(qProduct.price.multiply(qProduct.discount).divide(100)).between(min, max));
        }
        log.info("임플플..3" + expression.toString());


        QueryResults<Tuple> results = jpaQueryFactory
                .select(qProduct.prodNo, qProduct.prodName, qProduct.descript, qProduct.discount, qProduct.price, qProduct.seller, qProduct.delivery,
                        qProduct.thumb1, qProduct.sold, qProduct.score, qProduct.review, qProduct.rdate, qProduct.cate1, qProduct.cate2, qProduct.cate3)
                .from(qProduct)
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifier)
                .fetchResults();

        long total = results.getTotal();
        List<Tuple> content = results.getResults();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<Tuple> selectReviewByRdate(String uid) {
        QueryResults<Tuple> results = jpaQueryFactory
                .select(qReview, qProduct.prodName, qProduct.cate1, qProduct.cate2)
                .from(qReview)
                .where(qReview.uid.eq(uid))
                .join(qProduct).on(qReview.prodNo.eq(qProduct.prodNo))
                .orderBy(qReview.rdate.desc())// rdate를 기준으로 내림차순으로 정렬
                .limit(5)
                .fetchResults();

        return results.getResults();
    }


}

    


