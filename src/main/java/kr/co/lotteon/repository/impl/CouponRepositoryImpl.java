package kr.co.lotteon.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import groovy.lang.Tuple;
import kr.co.lotteon.entity.member.Coupon;
import kr.co.lotteon.entity.member.QCoupon;
import kr.co.lotteon.repository.custom.CouponRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QCoupon qCoupon = QCoupon.coupon;

    @Override
    public List<Coupon> selectsCouponsNotUse(String uid) {
        List<Coupon> result = jpaQueryFactory.select(qCoupon).from(qCoupon)
                                            .where(qCoupon.uid.eq(uid).and(qCoupon.useYn.eq("Y")))
                                            .fetch();
        log.info("임플 : " + result);
        return result;
    }
}
