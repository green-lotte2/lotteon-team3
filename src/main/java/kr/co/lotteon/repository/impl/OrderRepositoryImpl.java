package kr.co.lotteon.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.entity.product.QOrder;
import kr.co.lotteon.repository.custom.OrderRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QOrder qOrder =QOrder.order;

    // 월별 주문 count 조회 - 오늘 기준 12개월 전 까지
    @Override
    public List<Tuple> selectOrderForChart(){

        LocalDateTime twelveMonthsAgo = LocalDateTime.now().minusMonths(12);
        log.info("월별 주문 count 조회 Impl 1 : " + twelveMonthsAgo);

        return jpaQueryFactory.select(qOrder.ordDate.year(), qOrder.ordDate.month(), qOrder.count())
                .from(qOrder)
                .where(qOrder.ordDate.after(twelveMonthsAgo))
                .groupBy(qOrder.ordDate.year(), qOrder.ordDate.month())
                .orderBy(qOrder.ordDate.year().asc(), qOrder.ordDate.month().asc())
                .fetch();
    }
}
