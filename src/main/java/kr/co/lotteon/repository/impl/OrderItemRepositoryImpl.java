package kr.co.lotteon.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.admin.AdminPageRequestDTO;
import kr.co.lotteon.dto.product.PageRequestDTO;
import kr.co.lotteon.entity.product.*;
import kr.co.lotteon.repository.custom.OrderItemRepositoryCustom;
import kr.co.lotteon.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QOrderItem qOrderItem = QOrderItem.orderItem;
    private final QOrder qOrder = QOrder.order;
    private final QProduct qProduct = QProduct.product;
    private final QOption qOption = QOption.option;

    // 월별 주문 count 조회 - 오늘 기준 12개월 전 까지
    @Override
    public List<Tuple> selectOrderForChart(){

        LocalDateTime twelveMonthsAgo = LocalDateTime.now().minusMonths(12);
        log.info("월별 주문 count 조회 Impl 1 : " + twelveMonthsAgo);

        return jpaQueryFactory.select(qOrderItem.ordDate.year(), qOrderItem.ordDate.month(), qOrderItem.count())
                .from(qOrderItem)
                .where(qOrderItem.ordDate.after(twelveMonthsAgo))
                .groupBy(qOrderItem.ordDate.year(), qOrderItem.ordDate.month())
                .orderBy(qOrderItem.ordDate.year().asc(), qOrderItem.ordDate.month().asc())
                .fetch();
    }
    // 판매자 주문현황 - 주문 내역 Count 기간별 조회
    @Override
    public List<Tuple> selectSumByPeriod(List<Integer> prodNos){
        log.info("월별 주문 count 조회 Impl 1 : " + prodNos);

        LocalDateTime twelveMonthsAgo = LocalDateTime.now().minusMonths(12);

        return jpaQueryFactory.select(Expressions.stringTemplate("GROUP_CONCAT(CONCAT({0}, '-', {1}))", qOrderItem.ordDate.year(), qOrderItem.ordDate.month()), qProduct.count() )
                .from(qOrderItem)
                .where(qOrderItem.ordDate.after(twelveMonthsAgo).and(qOrderItem.prodNo.in(prodNos)))
                .groupBy(Expressions.stringTemplate("GROUP_CONCAT(CONCAT({0}, '-', {1}))", qOrderItem.ordDate.year(), qOrderItem.ordDate.month()))
                .orderBy(qOrderItem.ordDate.year().asc(), qOrderItem.ordDate.month().asc())
                .fetch();

    }
    // 판매자 인덱스 - 주문, 매출 내역 기간별 조회
    public Tuple selectCountSumByPeriod(LocalDateTime period, List<Integer> prodNos){
        log.info("월별 주문 count 조회 Impl 1 : " + prodNos);


         List<Tuple> tuples =  jpaQueryFactory.select(qOrderItem.prodNo, qProduct.price)
                .from(qOrderItem)
                .join(qProduct).on(qOrderItem.prodNo.eq(qProduct.prodNo))
                .where(qOrderItem.ordDate.after(period).and(qOrderItem.prodNo.in(prodNos)))
                .fetch();

         log.info("tuples :  "+ tuples);

         return jpaQueryFactory.select( qOrderItem.count(), qProduct.price.sum())
                .from(qOrderItem)
                .join(qProduct).on(qOrderItem.prodNo.eq(qProduct.prodNo))
                .where(qOrderItem.ordDate.after(period).and(qOrderItem.prodNo.in(prodNos)))
                .fetchOne();
    }
    // 판매자 인덱스 - 배송 현황 조회
    public List<Tuple> selectOrdStatusCount(List<Integer> prodNos){
        log.info("배송 현황 count 조회 Impl 1 : " + prodNos);

        return jpaQueryFactory.select(qOrderItem.ordStatus, qOrderItem.count())
                .from(qOrderItem)
                .where(qOrderItem.prodNo.in(prodNos))
                .groupBy(qOrderItem.ordStatus)
                .fetch();
    }

    // 판매자 주문 현황 월별 주문 count 조회 - 오늘 기준 12개월 전 까지
    @Override
    public List<Tuple> selectOrderForSeller(List<Integer> prodNos){

        LocalDateTime twelveMonthsAgo = LocalDateTime.now().minusMonths(12);
        log.info("월별 주문 count 조회 Impl 1 : " + twelveMonthsAgo);

        return jpaQueryFactory.select(qOrderItem.ordDate.year(), qOrderItem.ordDate.month() , qOrderItem.count())
                .from(qOrderItem)
                .where(qOrderItem.ordDate.after(twelveMonthsAgo).and(qOrderItem.prodNo.in(prodNos)))
                .groupBy(qOrderItem.ordDate.year(), qOrderItem.ordDate.month())
                .orderBy(qOrderItem.ordDate.year().asc(), qOrderItem.ordDate.month().asc())
                .fetch();
    }

    // 판매자 주문 현황 리스트 기본 조회
    @Override
    public Page<Tuple> selectOrderList(AdminPageRequestDTO pageRequestDTO, Pageable pageable, List<Integer> prodNos){
        log.info("판매자 주문 현황 조회 Impl 1 : " + pageRequestDTO);
        log.info("판매자 주문 현황 조회 Impl 2 : " + prodNos);

        QueryResults<Tuple> results =  jpaQueryFactory.select(qOrderItem, qOrder, qProduct, qOption)
                .from(qOrderItem)
                .join(qOrder).on(qOrderItem.ordNo.eq(qOrder.ordNo))
                .join(qProduct).on(qOrderItem.prodNo.eq(qProduct.prodNo))
                .leftJoin(qOption).on(qOption.opNo.eq(qOrderItem.opNo))
                .where(qOrderItem.prodNo.in(prodNos))
                .orderBy(qOrderItem.ordItemno.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        log.info("판매자 주문 현황 리스트 검색 조회 Impl 3 : " + results);
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
    // 판매자 주문 현황 리스트 검색 조회
    public Page<Tuple> searchOrderList(AdminPageRequestDTO pageRequestDTO, Pageable pageable, List<Integer> prodNos){
        log.info("판매자 주문 현황 검색 Impl 1 : " + pageRequestDTO);
        log.info("판매자 주문 현황 검색 Impl 2 : " + prodNos);
        log.info("상품 목록 키워드 검색 impl 3 : " + pageRequestDTO.getKeyword());
        String keyword = pageRequestDTO.getKeyword();
        String type = pageRequestDTO.getType();

        BooleanExpression expression = null;

        // 검색 종류에 따른 where절 표현식 생성
        if(type.equals("prodName")){
            expression = qProduct.prodName.contains(keyword).and(qOrderItem.prodNo.in(prodNos));
            log.info("prodName 검색 : " + expression);

        }else if(type.equals("prodNo")){
            // 입력된 키워드를 정수형으로 변환
            int prodNo = Integer.parseInt(keyword);
            expression = qProduct.prodNo.eq(prodNo).and(qOrderItem.prodNo.in(prodNos));
            log.info("prodNo 검색 : " + expression);

        }else if(type.equals("cate1")){
            int cate1 = Integer.parseInt(keyword);
            expression = qProduct.cate1.eq(cate1).and(qOrderItem.prodNo.in(prodNos));
            log.info("cate1 검색 : " + expression);

        }else if(type.equals("company")){
            expression = qProduct.company.contains(keyword).and(qOrderItem.prodNo.in(prodNos));
            log.info("company 검색 : " + expression);
        }else if(type.equals("ordStatus")){
            expression = qOrderItem.ordStatus.contains(keyword).and(qOrderItem.prodNo.in(prodNos));
            log.info("ordStatus 검색 : " + expression);
        }

        QueryResults<Tuple> results =  jpaQueryFactory.select(qOrderItem, qOrder, qProduct, qOption)
                .from(qOrderItem)
                .join(qOrder).on(qOrderItem.ordNo.eq(qOrder.ordNo))
                .join(qProduct).on(qOrderItem.prodNo.eq(qProduct.prodNo))
                .leftJoin(qOption).on(qOption.opNo.eq(qOrderItem.opNo))
                .where(expression)
                .orderBy(qOrderItem.ordItemno.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        log.info("판매자 주문 현황 조회 Impl 3 : " + results);
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
    // 관리자 주문 현황
    @Override
    public Page<Tuple> selectOrderListAll(AdminPageRequestDTO pageRequestDTO, Pageable pageable){
        log.info("관리자 주문 현황 조회 Impl 1 : " + pageRequestDTO);

        QueryResults<Tuple> results =  jpaQueryFactory.select(qOrderItem, qOrder, qProduct, qOption)
                .from(qOrderItem)
                .join(qOrder).on(qOrderItem.ordNo.eq(qOrder.ordNo))
                .join(qProduct).on(qOrderItem.prodNo.eq(qProduct.prodNo))
                .leftJoin(qOption).on(qOption.opNo.eq(qOrderItem.opNo))
                .orderBy(qOrderItem.ordItemno.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        log.info("관리자 주문 현황 조회 Impl 2 : " + results);
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
    // 관리자 주문 현황 리스트 검색 조회
    @Override
    public Page<Tuple> searchOrderListAll(AdminPageRequestDTO pageRequestDTO, Pageable pageable) {
        log.info("관리자 주문 현황 검색 Impl 1 : " + pageRequestDTO);
        log.info("관리자 주문 현황 검색 Impl 2 : " + pageRequestDTO.getType());
        log.info("상품 목록 키워드 검색 impl 3 : " + pageRequestDTO.getKeyword());
        String keyword = pageRequestDTO.getKeyword();
        String type = pageRequestDTO.getType();

        BooleanExpression expression = null;

        // 검색 종류에 따른 where절 표현식 생성
        if (type.equals("prodName")) {
            expression = qProduct.prodName.contains(keyword);
            log.info("prodName 검색 : " + expression);

        } else if (type.equals("prodNo")) {
            // 입력된 키워드를 정수형으로 변환
            int prodNo = Integer.parseInt(keyword);
            expression = qProduct.prodNo.eq(prodNo);
            log.info("prodNo 검색 : " + expression);

        } else if (type.equals("cate1")) {
            int cate1 = Integer.parseInt(keyword);
            expression = qProduct.cate1.eq(cate1);
            log.info("cate1 검색 : " + expression);

        } else if (type.equals("company")) {
            expression = qProduct.company.contains(keyword);
            log.info("company 검색 : " + expression);
        } else if (type.equals("seller")) {
            expression = qProduct.seller.contains(keyword);
            log.info("seller 검색 : " + expression);
        } else if (type.equals("ordStatus")) {
            expression = qOrderItem.ordStatus.contains(keyword);
            log.info("ordStatus 검색 : " + expression);
        }

        QueryResults<Tuple> results = jpaQueryFactory.select(qOrderItem, qOrder, qProduct, qOption)
                .from(qOrderItem)
                .join(qOrder).on(qOrderItem.ordNo.eq(qOrder.ordNo))
                .join(qProduct).on(qOrderItem.prodNo.eq(qProduct.prodNo))
                .leftJoin(qOption).on(qOption.opNo.eq(qOrderItem.opNo))
                .where(expression)
                .orderBy(qOrderItem.ordItemno.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        log.info("관리자 주문 현황 리스트 검색 조회 Impl 3 : " + results);
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }


    @Override
    public int countByUidAndOrdStatusIn(String uid, List<String> ordStatusList) {
        return (int) jpaQueryFactory
                .select(qOrderItem)
                .from(qOrderItem)
                .where(qOrderItem.uid.eq(uid)
                        .and(qOrderItem.ordStatus.in(ordStatusList)))
                .fetchCount();
    }
}
