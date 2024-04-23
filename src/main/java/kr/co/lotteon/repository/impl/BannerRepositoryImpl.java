package kr.co.lotteon.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.entity.admin.Banner;
import kr.co.lotteon.entity.admin.QBanner;
import kr.co.lotteon.repository.custom.BannerRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BannerRepositoryImpl implements BannerRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QBanner qBanner = QBanner.banner;

    // 활성화 상태인 배너 select
    @Override
    public List<Banner> selectByCateAndAct(String cate){

        List<Banner> banners = jpaQueryFactory
                .selectFrom(qBanner)
                .where(qBanner.cate.eq(cate).and(qBanner.activation.eq(1)))
                .orderBy(qBanner.bno.desc())
                .fetch();

        return banners;
    }
}
