package kr.co.lotteon.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.entity.product.QCart;
import kr.co.lotteon.entity.product.QOrder;
import kr.co.lotteon.entity.product.QProduct;
import kr.co.lotteon.repository.custom.OrderRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QOrder qOrder = QOrder.order;
    private final QProduct qProduct = QProduct.product;
    private final QCart qCart = QCart.cart;

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

    // 장바구니에서 오더
    @Override
    public List<Tuple> selectOrderFromCart(int cartNo) {
        return jpaQueryFactory.select(qCart.count,qCart.opNo, qProduct)
                .from(qCart)
                .join(qProduct)
                .on(qCart.prodNo.eq(qProduct.prodNo))
                .where(qCart.cartNo.eq(cartNo))
                .fetch();
    }

}
