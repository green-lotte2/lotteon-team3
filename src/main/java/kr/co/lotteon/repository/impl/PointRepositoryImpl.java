package kr.co.lotteon.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.entity.member.Point;
import kr.co.lotteon.entity.member.QPoint;
import kr.co.lotteon.repository.custom.PointRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
@Slf4j
@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QPoint qPoint = QPoint.point1;

    @Override
    public List<Point> selectPointByUidAndDate(String uid) {

        return jpaQueryFactory
                .select(qPoint)
                .from(qPoint)
                .where(qPoint.uid.eq(uid))
                .orderBy(qPoint.currentDate.desc())
                .limit(5)
                .fetch();
    }
}
