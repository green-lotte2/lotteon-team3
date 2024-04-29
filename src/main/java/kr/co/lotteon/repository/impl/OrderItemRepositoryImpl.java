package kr.co.lotteon.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.entity.product.QOrder;
import kr.co.lotteon.entity.product.QOrderItem;
import kr.co.lotteon.entity.product.QProduct;
import kr.co.lotteon.repository.custom.OrderItemRepositoryCustom;
import kr.co.lotteon.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QOrderItem qOrderItem = QOrderItem.orderItem;
    private final QOrder qOrder = QOrder.order;
    private final QProduct qProduct = QProduct.product;

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

}
