package kr.co.lotteon.repository.impl;


import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.admin.AdminProductPageRequestDTO;
import kr.co.lotteon.dto.product.PageRequestDTO;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.product.QOption;
import kr.co.lotteon.entity.product.QProduct;
import kr.co.lotteon.repository.custom.ProductRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.beans.Expression;
import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QProduct qProduct = QProduct.product;
    private final QOption qOption = QOption.option;

    // 관리자 - 상품 목록 기본 조회
    @Override
    public Page<Product> adminSelectProducts(AdminProductPageRequestDTO adminProductPageRequestDTO, Pageable pageable){
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
    public Page<Product> adminSearchProducts(AdminProductPageRequestDTO adminProductPageRequestDTO, Pageable pageable){
        log.info("상품 목록 키워드 검색 impl 1 : " + adminProductPageRequestDTO.getKeyword());
        String type = adminProductPageRequestDTO.getType();
        String keyword = adminProductPageRequestDTO.getKeyword();

        BooleanExpression expression = null;

        // 검색 종류에 따른 where절 표현식 생성
        if(type.equals("prodName")){
            expression = qProduct.prodName.contains(keyword);
            log.info("prodName 검색 : " + expression);

        }else if(type.equals("prodCode")){
            // 입력된 키워드를 정수형으로 변환
            int prodCode = Integer.parseInt(keyword);
            expression = qProduct.prodCode.eq(prodCode);
            log.info("prodCode 검색 : " + expression);

        }else if(type.equals("cate1")){
            int cate1 = Integer.parseInt(keyword);
            expression = qProduct.cate1.eq(cate1);
            log.info("cate1 검색 : " + expression);

        }else if(type.equals("company")){
            expression = qProduct.company.contains(keyword);
            log.info("company 검색 : " + expression);

        }else if(type.equals("seller")){
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

        QueryResults<Product> results = jpaQueryFactory.selectFrom(qProduct)
                                .orderBy(qProduct.prodNo.desc())
                                .offset(pageable.getOffset())
                                .limit(pageable.getPageSize())
                                .where(predicate)
                                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public Map<String, List<String>> selectProdOption(int prodNo) {

        // SELECT opName, group_concat(opValue) from product_option where prodNo =? GROUP BY opName;
        List<Tuple> result = jpaQueryFactory
                .select(qOption.opName, Expressions.stringTemplate("GROUP_CONCAT({0})",qOption.opValue))
                .from(qOption)
                .where(qOption.prodNo.eq(prodNo))
                .groupBy(qOption.opName)
                .fetch();

        log.info("impl 1" + result);

        Map<String, List<String>> resultMap = new HashMap<>();

        for (Tuple tuple : result) {
            String opName = tuple.get(qOption.opName);
            log.info("opName : " + opName);
            String opValue = tuple.get(Expressions.stringTemplate("GROUP_CONCAT({0})", qOption.opValue));
            log.info("opValue : " + opValue);

            // opValue를 배열로 만들기
            List<String> opValueList = Arrays.asList(opValue.split(","));
            log.info("opValue List impl 2" + opValueList);

            // opName이 이미 resultMap에 존재하는지 확인, 없으면 빈 리스트를 새로 생성하여 추가
            resultMap.putIfAbsent(opName, new ArrayList<>());

            resultMap.put(opName, opValueList);
        }
        log.info("impl 3" + resultMap);

        return resultMap;
    }

    // 관리자 - 의류 옵션 추가 상품 코드 조회
    public Product findProductByProdCode(int prodCode){
        log.info("의류 옵션 추가 Impl 1 : " + prodCode);
        Product product = jpaQueryFactory
                .selectFrom(qProduct)
                .where(qProduct.prodCode.eq(prodCode))
                .limit(1)
                .fetchOne();
        log.info("의류 옵션 추가 Impl 2 : " + product);
        return product;
    }

}


