package kr.co.lotteon.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.entity.product.QReview;
import kr.co.lotteon.repository.custom.ReviewRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QReview qReview = QReview.review;
    // 리뷰 비율 조회
    @Override
    public double selectReviewAvg(int prodNo){

        long count = jpaQueryFactory.select(qReview.count())
                .from(qReview)
                .where(qReview.prodNo.eq(prodNo))
                .fetchOne();

        if(count != 0) {
            double avg = jpaQueryFactory.select(qReview.rating.avg())
                    .from(qReview)
                    .where(qReview.prodNo.eq(prodNo))
                    .fetchOne();

            return avg;
        }else{
            return 0.0;
        }
    }

    @Override
    public int selectReviewCountByUid(String uid, int ordNo, int prodNo) {
        return (int) jpaQueryFactory
                .select(qReview)
                .from(qReview)
                .where(qReview.uid.eq(uid).and(qReview.prodNo.eq(prodNo).and(qReview.ordNo.eq(ordNo))))
                .fetchCount();
    }


}
