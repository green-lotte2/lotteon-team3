package kr.co.lotteon.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.entity.member.QMember;
import kr.co.lotteon.entity.product.QOrder;
import kr.co.lotteon.repository.custom.MemberRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QMember qMember = QMember.member;

    // 월별 가입 count 조회 - 오늘 기준 12개월 전 까지
    @Override
    public List<Tuple> selectMemberForChart(){

        LocalDateTime twelveMonthsAgo = LocalDateTime.now().minusMonths(12);
        log.info("월별 가입 count 조회 Impl 1 : " + twelveMonthsAgo);

        return jpaQueryFactory.select(qMember.rdate.year(), qMember.rdate.month(), qMember.count())
                .from(qMember)
                .where(qMember.rdate.after(twelveMonthsAgo))
                .groupBy(qMember.rdate.year(), qMember.rdate.month())
                .orderBy(qMember.rdate.year().asc(), qMember.rdate.month().asc())
                .fetch();
    }
}
